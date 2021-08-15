package com.study.deadletter.consumer;

import com.alibaba.fastjson.JSONObject;
import com.study.deadletter.mapper.OrderMapper;
import com.study.deadletter.model.OrderEntity;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 监听我们的订单队列
     */
    @RabbitListener(queues = "study_order_queue")
    public void orderConsumer(String msg) {
        System.out.println("订单队列获取消息:" + msg);
        OrderEntity orderEntity = JSONObject.parseObject(msg, OrderEntity.class);
        if (orderEntity == null) {
            return;
        }
        orderMapper.addOrder(orderEntity);
    }
}
