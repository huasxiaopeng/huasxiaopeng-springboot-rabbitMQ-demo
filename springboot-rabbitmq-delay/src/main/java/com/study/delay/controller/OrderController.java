package com.study.delay.controller;

import com.study.delay.mapper.OrderMapper;
import com.study.delay.model.Orders;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建producer生产者.暂时设置消息10秒过期,验证消息是否加入死信队列
 */
@RestController
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${study.order.exchange}")
    private String orderExchange; //订单交换机

    @Value("${study.order.routingKey}")
    private String orderRoutingKey; //订单路由key

    @GetMapping("/addOrder")
    public String addOrder() {
        String orderId = System.currentTimeMillis() + "";
        Orders orderEntity = new Orders("订单30分钟过期", orderId, 0);
        int result = orderMapper.addOrder(orderEntity);
        if (result <= 0) {
            return "fail";
        }
        rabbitTemplate.convertAndSend(orderExchange, orderRoutingKey, orderId, messagePostProcessor());
        return "success";
    }

    //处理待发送消息
    private MessagePostProcessor messagePostProcessor() {
        return new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                MessageProperties messageProperties = message.getMessageProperties();
                // 设置编码
                messageProperties.setContentEncoding("utf-8");
                //设置有效期30分钟
                //messageProperties.setExpiration("1800000");
                messageProperties.setExpiration("10000");
                return message;
            }
        };
    }
}
