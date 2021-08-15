package com.study.delay.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeadLetterMQConfig {

    /**
     * 订单交换机
     */
    @Value("${study.order.exchange}")
    private String orderExchange;

    /**
     * 订单队列
     */
    @Value("${study.order.queue}")
    private String orderQueue;

    /**
     * 订单路由key
     */
    @Value("${study.order.routingKey}")
    private String orderRoutingKey;

    /**
     * 死信交换机
     */
    @Value("${study.dlx.exchange}")
    private String dlxExchange;

    /**
     * 死信队列
     */
    @Value("${study.dlx.queue}")
    private String dlxQueue;
    /**
     * 死信路由
     */
    @Value("${study.dlx.routingKey}")
    private String dlxRoutingKey;

    /**
     * 声明死信交换机
     */
    @Bean(name = "dlxExchange")
    public DirectExchange dlxExchange() {
        return new DirectExchange(dlxExchange);
    }

    /**
     * 声明死信队列
     */
    @Bean(name = "dlxQueue")
    public Queue dlxQueue() {
        return new Queue(dlxQueue);
    }

    /**
     * 绑定死信队列到死信交换机
     */
    @Bean
    public Binding binding(@Qualifier(value = "dlxExchange")DirectExchange directExchange,@Qualifier(value = "dlxQueue")Queue dlxQueue ) {
        return BindingBuilder.bind(dlxQueue).to(directExchange).with(dlxRoutingKey);
    }

    /**
     * 声明订单业务交换机
     */
    @Bean(name = "orderExchange")
    public DirectExchange orderExchange() {
        return new DirectExchange(orderExchange);
    }

    /**
     * 声明订单队列 核心操作一
     */
    @Bean(name="orderQueue")
    public Queue orderQueue() {
        //声明队列的类型
        Map<String, Object> arguments = new HashMap<>(2);
        // 绑定我们的死信交换机
        arguments.put("x-dead-letter-exchange", dlxExchange);
        // 绑定我们的路由key
        arguments.put("x-dead-letter-routing-key", dlxRoutingKey);
        return new Queue(orderQueue, true, false, false, arguments);
    }
    /**
     * 绑定订单队列到订单交换机
     */
    @Bean
    public Binding orderBinding(@Qualifier(value = "orderExchange")DirectExchange orderExchange ,@Qualifier(value = "orderQueue") Queue orderQueue) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(orderRoutingKey);
    }

}
