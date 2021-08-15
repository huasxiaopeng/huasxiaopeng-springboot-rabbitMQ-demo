package com.study.work.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 消费者
 * @Version 1.0
 */
@Component
@RabbitListener(queues = "fanout_sms_queue") //监听哪个队列
public class FanoutSmsConsumer {

    /**
     * 监听回调的方法
     */
    @RabbitHandler
    public void process(String msg) {
        System.out.println("短信消费者消息msg:  " + msg + "      " + new Date());

        int i = 1 / 0;

    }
}
