package com.study.dlconsumer.consumer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.study.dlconsumer.mapper.OrderMapper;
import com.study.dlconsumer.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
public class OrderConsumer {
    @Autowired
    private OrderMapper orderMapper;
    /***
     * 补单的消费不应该和 订单生产者放到一个服务器节点
     * 补单消费者如果不存在的情况下 队列缓存补单消息
     * 补偿分布式事务解决框架 思想最终一致性
     */
    @RabbitListener(queues = "order_create_queue")
    public void dispatchConsumer(Message message , Channel channel) throws IOException {
        // 1.获取消息
        String msg = new String(message.getBody());
        // 2.转换json
        Order orderEntity = JSONObject.parseObject(msg, Order.class);
        String orderId = orderEntity.getOrderId();
        /**
         * 查询定义信息，发现不存在，则需要进行补单操作
         */
        Order result = orderMapper.findOrderId(orderId);
        if (null!=result ) {
            // 手动将该消息删除
            // 手动ack 删除该消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("<<<消费者消费成功>>> 时间: {}",new Date());
            return;
        }
        //进行补单操作
        int i = orderMapper.addOrder(orderEntity);
        if(i>0){
            // 手动将该消息删除
            // 手动ack 删除该消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("<<<消费者补单消费成功>>> 时间: {}",new Date());
        }
    }
}
