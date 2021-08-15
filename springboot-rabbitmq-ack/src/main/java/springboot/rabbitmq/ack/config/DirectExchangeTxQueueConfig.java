package springboot.rabbitmq.ack.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lktbz
 * @version 1.0.0
 * @date 2021/8/15
 * @desc
 */
@Configuration
public class DirectExchangeTxQueueConfig {
    /**
     * 定义交换机
     * @return
     */
    @Bean(name="getDirectExchangeTx")
    public DirectExchange getDirectExchangeTx(){
        return new DirectExchange("directExchangeTx", true, false);
    }

    /**
     * 定义队列
     * @return
     */
    @Bean(name="getQueueTx")
    public Queue getQueueTx(){
        return new Queue("directQueueTx", true, false, false);
    }

    /**
     * 定义 交换机与队列的绑定关系绑定关系
     * @param getDirectExchangeTx
     * @param getQueueTx
     * @return
     */
    @Bean
    public Binding getDirectExchangeQueueTx(
            @Qualifier(value="getDirectExchangeTx") DirectExchange getDirectExchangeTx,
            @Qualifier(value="getQueueTx") Queue getQueueTx){
        return BindingBuilder.bind(getQueueTx).to(getDirectExchangeTx).with("directQueueTxRoutingKey");
    }

}
