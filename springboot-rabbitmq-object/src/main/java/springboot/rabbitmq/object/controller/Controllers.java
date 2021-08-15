package springboot.rabbitmq.object.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.rabbitmq.object.producers.UserProducer;

/**
 * @author lktbz
 * @version 1.0.0
 * @date 2021/8/15
 * @desc
 */
@RestController
public class Controllers {
    @Autowired
    private UserProducer userProducer;
    @RequestMapping("/user")
    public String sendOrder() throws JsonProcessingException {
        return userProducer.sendOrders();
    }
}
