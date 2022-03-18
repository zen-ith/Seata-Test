package com.demo.controller;

import com.demo.queue.BusinessQueueConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/business")
@CrossOrigin
public class BusinessController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 增加秒杀订单
     *
     * @param account
     * @param id
     * @param price
     * @return
     */
    @PostMapping(value = "/addorder")
    public Map<String, Object> add(
            @RequestParam(value = "account") String account,
            @RequestParam(value = "id") int id,
            @RequestParam(value = "price") int price) {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        Map<String, Object> businessInfo = new HashMap<String, Object>();
        businessInfo.put("account", account);
        businessInfo.put("id", id);
        businessInfo.put("price", price);

        // 调用redis给相应商品库存量减一
        if (stringRedisTemplate.opsForValue().decrement("ITEM_" + businessInfo.get("id")) >= 0) {
            // 库存足够，发给消息队列
            rabbitTemplate.convertAndSend(BusinessQueueConfig.BUSINESS_EXCHANGE, BusinessQueueConfig.BUSINESS_ROUTING_KEY, businessInfo);
        } else {
            resultMap.put("result", false);
            resultMap.put("msg", "在库不足。");
            return resultMap;
        }

        resultMap.put("result", true);
        resultMap.put("msg", "");
        return resultMap;
    }

}
