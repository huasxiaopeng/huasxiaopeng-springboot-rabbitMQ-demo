package springboot.rabbitmq.object.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

/**
 * @author lktbz
 * @version 1.0.0
 * @date 2021/8/15
 * @desc
 */
@Configuration
public class RabbitConfig  implements RabbitListenerConfigurer {
    /**
     * 定义交换机
     * @return
     */
    @Bean(name="getDirectObject")
    public DirectExchange getDirectExchangeTx(){
        return new DirectExchange("Object-tx", true, false);
    }

    /**
     * 定义队列
     * @return
     */
    @Bean(name="getObjectQueueTx")
    public Queue getObjectQueueTx(){
        return new Queue("objectQueueTx", true, false, false);
    }
    /**
     * 定义 交换机与队列的绑定关系绑定关系
     * @param getDirectExchangeTx
     * @param getQueueTx
     * @return
     */
    @Bean
    public Binding getDirectExchangeQueueTx(
            @Qualifier(value="getDirectObject") DirectExchange getDirectExchangeTx,
            @Qualifier(value="getObjectQueueTx") Queue getQueueTx){
        return BindingBuilder.bind(getQueueTx).to(getDirectExchangeTx).with("objectKey");
    }
    /**
     * 配置对象序列化协议
     * @param rabbitListenerEndpointRegistrar
     */
    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
        rabbitListenerEndpointRegistrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    MessageHandlerMethodFactory messageHandlerMethodFactory(){
        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        messageHandlerMethodFactory.setMessageConverter(mappingJackson2MessageConverter());
        return messageHandlerMethodFactory;
    }

    @Bean
    public MappingJackson2MessageConverter mappingJackson2MessageConverter(){
        return  new MappingJackson2MessageConverter();
    }


}
