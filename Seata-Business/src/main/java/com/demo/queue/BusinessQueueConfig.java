package com.demo.queue;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class BusinessQueueConfig {

    // 业务交换机
    public static final String BUSINESS_EXCHANGE = "BUSINESS_EXCHANGE";

    // 死信交换机
    public static final String DEAD_LETTER_EXCHANGE = "DEAD_LETTER_EXCHANGE";

    // 业务队列
    public static final String BUSINESS_QUEUE = "BUSINESS_QUEUE";

    // 死信队列
    public static final String DEAD_LETTER_QUEUE = "DEAD_LETTER_QUEUE";

    // 业务路由键
    public static final String BUSINESS_ROUTING_KEY = "BUSINESS_ROUTING_KEY";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 创建业务交换机
    @Bean
    public DirectExchange getOrderExchange() {
        return ExchangeBuilder.directExchange(BUSINESS_EXCHANGE).durable(true).build();
    }

    // 创建死信交换机
    @Bean
    public DirectExchange getDeadLetterExchange() {
        return ExchangeBuilder.directExchange(DEAD_LETTER_EXCHANGE).durable(true).build();
    }

    // 创建业务队列
    @Bean
    public Queue getOrderQueue() {
        Map<String, Object> args = new HashMap<>(2);
        // 设置业务队列的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        return QueueBuilder.durable(BUSINESS_QUEUE).withArguments(args).build();
    }

    // 创建死信队列
    @Bean
    public Queue getDeadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    // 业务队列与业务交换机进行绑定
    @Bean
    public Binding bindBusinessQueue() {
        return BindingBuilder.bind(getOrderQueue()).to(getOrderExchange()).with(BUSINESS_ROUTING_KEY);
    }

    // 业务队列与业务交换机进行绑定
    @Bean
    public Binding bindDeadLetterQueue() {
        return BindingBuilder.bind(getDeadLetterQueue()).to(getDeadLetterExchange()).with(BUSINESS_ROUTING_KEY);
    }
}
