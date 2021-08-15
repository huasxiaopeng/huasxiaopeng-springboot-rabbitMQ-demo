package springboot.rabbitmq.object.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springboot.rabbitmq.object.entitty.User;

import javax.jws.soap.SOAPBinding;

/**
 * @author lktbz
 * @version 1.0.0
 * @date 2021/8/15
 * @desc   就不写客户端了，直接在服务端admin 上查看消息是否发送成功就可以
 */
@Component
public class UserProducer implements RabbitTemplate.ConfirmCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    public String sendOrders() throws JsonProcessingException {
        User user=new User(2L,"LKTZ");
        String s = objectMapper.writeValueAsString(user);
        rabbitTemplate.convertAndSend("Object-tx","objectKey",s);
        return "success";
    }
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

    }
}
