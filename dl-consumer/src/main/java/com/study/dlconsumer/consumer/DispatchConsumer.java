package com.study.dlconsumer.consumer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.study.dlconsumer.mapper.DispatchMapper;
import com.study.dlconsumer.model.Dispatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
public class DispatchConsumer {

    @Autowired
    private DispatchMapper dispatchMapper;

    @RabbitListener(queues = "order_dic_queue")
    public void dispatchConsumer(Message message , Channel channel) throws IOException {
        // 1.获取消息
        String msg = new String(message.getBody());
        // 2.转换json
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String orderId = jsonObject.getString("orderId");
        //主动根据orderId查询派单是否已经派单过,如果派单,不走下面插入代码
        // 计算分配的快递员id
        Dispatch dispatchEntity = new Dispatch(orderId, 1234L);
        // 3.插入我们的数据库
        int result = dispatchMapper.insertDistribute(dispatchEntity);
        if (result > 0) {
            // 手动将该消息删除
            // 手动ack 删除该消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("<<<消费者派单消费成功>>> 时间: {} " ,new Date());
        }
    }
}
