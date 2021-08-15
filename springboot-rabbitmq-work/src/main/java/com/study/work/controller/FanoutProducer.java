package com.study.work.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产者
 */
@RestController
public class FanoutProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //定义短信队列
    private String FANOUT_SMS_QUEUE = "fanout_sms_queue";

    //定义交换机
    private String EXCHANG_SPRINGBOOT_NAME = "springboot_exchange";

    //定义交换机
    private String EXCHANG_LKTBZ_NAME="lktbz_exchange";
    @GetMapping("/sendMsg")
    public String sendMsg() {
        //投递消息 客户端不会马上知道消息是否被消费,但是能够确认知道我们是否投递到消息中间件
        //参数1: 队列名  参数2: 发送消息内容
        //rabbitTemplate.convertAndSend(FANOUT_SMS_QUEUE,"发送队列消息 1");

        //参数1: 交换机名称 参数2: 路由key 参数3: 消息
        rabbitTemplate.convertAndSend(EXCHANG_SPRINGBOOT_NAME, "", "发送交换机消息 2");
        return "SUCCESS";
    }

}
