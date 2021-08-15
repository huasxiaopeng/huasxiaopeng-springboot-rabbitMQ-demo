package com.study.deadletter.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component //死信队列
public class OrderDlxConsumer {

    /**
     * 监听我们的死信队列
     */
    @RabbitListener(queues = "study_order_dlx_queue")
    public void orderConsumer(String msg) {
        System.out.println("死信队列获取消息:" + msg);
    }
}
