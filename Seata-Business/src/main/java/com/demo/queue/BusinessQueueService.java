package com.demo.queue;

import com.demo.service.BusinessService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class BusinessQueueService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BusinessService businessService;

    @Value("${QUEUES_RETRY_COUNT}")
    private int queuesRetryCount;

    /**
     * 监听消息队列，增加秒杀订单
     *
     * @param businessInfo
     * @param message
     * @param channel
     */
    @RabbitListener(queues = BusinessQueueConfig.BUSINESS_QUEUE)
    public void add(Map<String, Object> businessInfo, Message message, Channel channel) {

        log.info("收到秒杀订单消息：{}", businessInfo);

        Map<String, Object> resultMap;
        boolean isSuccess = false;
        int retryCount = queuesRetryCount;

        while (!isSuccess && retryCount-- > 0) {
            try {
                // 创建秒杀订单
                resultMap = businessService.add(
                        (String) businessInfo.get("account"),
                        (int) businessInfo.get("id"),
                        1, // 固定
                        (int) businessInfo.get("price")
                );

                if (!(Boolean) resultMap.get("result")) {
                    throw new Exception("创建订单失败。");
                }

                // 正常处理完毕，手动确认
                isSuccess = true;
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (Exception e) {
                log.error("程序异常：", e);
            }
        }

        // 达到最大重试次数后仍然消费失败
        if (!isSuccess) {
            // 手动删除，移至死信队列
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}