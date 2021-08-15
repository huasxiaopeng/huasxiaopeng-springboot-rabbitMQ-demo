package com.study.dlconsumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OrderRabbitMQConfig {

    @Autowired
    RabbitAdmin rabbitAdmin;

    /**
     * 派单队列
     */
    public static final String ORDER_DIC_QUEUE = "order_dic_queue";
    /**
     * 补单对接
     */
    public static final String ORDER_CREATE_QUEUE = "order_create_queue";
    /**
     * 订单交换机
     */
    private static final String ORDER_EXCHANGE_NAME = "order_exchange_name";

    /**
     * 定义派单队列
     */
    @Bean(name="directOrderDicQueue")
    public Queue directOrderDicQueue() {
        return new Queue(ORDER_DIC_QUEUE);
    }

    /**
     * 定义补派单队列
     */
    @Bean(name="directCreateOrderQueue")
    public Queue directCreateOrderQueue() {
        return new Queue(ORDER_CREATE_QUEUE);
    }


    /**
     * 定义订单交换机
     */
    @Bean(name="directOrderExchange")
    DirectExchange directOrderExchange() {
        return new DirectExchange(ORDER_EXCHANGE_NAME);
    }

    /**
     * 派单队列与交换机绑定
     */
    @Bean
    Binding bindingExchangeOrderDicQueue(@Qualifier(value ="directOrderDicQueue")Queue directOrderDicQueue, @Qualifier(value = "directOrderExchange" )DirectExchange directOrderExchange) {
        return BindingBuilder.bind(directOrderDicQueue).to(directOrderExchange).with("orderRoutingKey");
    }

    /**
     * 补单队列与交换机绑定
     */
    @Bean
    Binding bindingExchangeCreateOrder(@Qualifier(value ="directCreateOrderQueue")Queue directCreateOrderQueue, @Qualifier(value = "directOrderExchange" )DirectExchange directOrderExchange) {
        return BindingBuilder.bind(directCreateOrderQueue).to(directOrderExchange).with("orderRoutingKey");
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public void createExchangeQueue ( DirectExchange directOrderExchange,Queue directOrderDicQueue, Queue directCreateOrderQueue){
        rabbitAdmin.declareExchange(directOrderExchange);
        rabbitAdmin.declareQueue(directOrderDicQueue);
        rabbitAdmin.declareQueue(directCreateOrderQueue);
    }

}
