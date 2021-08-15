package com.study.work.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**

 * @Version 1.0
 */
@Component
public class RabbitMQConfig {

    //定义交换机
    private String EXCHANG_SPRINGBOOT_NAME="springboot_exchange";

    //定义短信队列
    private String FANOUT_SMS_QUEUE="fanout_sms_queue";

    //定义邮件队列
    private String FANOUT_EMAIL_QUEUE="fanout_email_queue";

    /**
     * 声明的sms队列
     * org.springframework.amqp.core.Queue
     */
    @Bean
    public Queue smsQueue(){
        return new Queue(FANOUT_SMS_QUEUE);
    }

    /**
     * 声明的email队列
     */
    @Bean
    public Queue emailQueue(){
        return new Queue(FANOUT_EMAIL_QUEUE);
    }

    /**
     * 声明交换机
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(EXCHANG_SPRINGBOOT_NAME);
    }

    /**
     * 短信队列绑定交换机
     * @param smsQueue 这里根据方法名找队列 名称一定要规范
     * @param fanoutExchange 这里根据方法名找交换机 名称一定要规范
     */
    @Bean
    public Binding smsBindingExchange(Queue smsQueue , FanoutExchange fanoutExchange){

        return BindingBuilder.bind(smsQueue).to(fanoutExchange);
    }

    /**
     * 邮件队列绑定交换机
     * @param emailQueue 这里根据方法名找队列 名称一定要规范
     * @param fanoutExchange 这里根据方法名找交换机 名称一定要规范
     */
    @Bean
    public Binding emailBindingExchange(Queue emailQueue , FanoutExchange fanoutExchange){
        return BindingBuilder.bind(emailQueue).to(fanoutExchange);
    }
}
