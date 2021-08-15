package com.study.work.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费者
 * @Version 1.0
 */
@Component
@RabbitListener(queues = "fanout_email_queue") //监听哪个队列
public class FanoutEmailConsumer {

    /**
     * 监听回调的方法
     */
    @RabbitHandler
    public void process(String msg){
        System.out.println("邮件消费者消息msg:  "+msg);
    }
}
