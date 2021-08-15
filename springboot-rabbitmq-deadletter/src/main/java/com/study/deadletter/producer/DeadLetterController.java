package com.study.deadletter.producer;

import com.alibaba.fastjson.JSONObject;
import com.study.deadletter.mapper.OrderMapper;
import com.study.deadletter.model.OrderEntity;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeadLetterController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 订单交换机
     */
    @Value("${study.order.exchange}")
    private String orderExchange;

    @Autowired
    private OrderMapper orderMapper;
    /**
     * 订单路由key
     */
    @Value("${study.order.routingKey}")
    private String orderRoutingKey;

//    //方式一
//    @RequestMapping("/sendOrderMsg")
//    public String sendOrderMsg() {
//        rabbitTemplate.convertAndSend(orderExchange,orderRoutingKey,"订单消息", new MessagePostProcessor(){
//            @Override
//            public Message postProcessMessage(Message message) throws AmqpException {
//                //消息设置过期时间
//                message.getMessageProperties().setExpiration("10000");
//                //消息设置唯一id
//               // message.getMessageProperties().setUserId("10000");
//                return message;
//            }
//        });
//        return "success";
//    }


    //方式二  优化
    @RequestMapping("/sendOrderMsg")
    public String sendOrderMsg() {
        // 1.生产订单id
        String orderId = System.currentTimeMillis() + "";
        String orderName = "咕泡教育-Java架构师";
        OrderEntity orderEntity = new OrderEntity(orderName, orderId);
        String msg = JSONObject.toJSONString(orderEntity);
        sendMsg(msg); //rabbitmq生产消息 异步处理
        return orderId;
        // 后期客户端主动使用orderId调用服务器接口 查询该订单id是否在数据库中存在数据 消费成功 消费失败
    }

    //rabbit发送消息
    @Async //异步处理
    public void sendMsg(String msg) {
        rabbitTemplate.convertAndSend(orderExchange, orderRoutingKey, msg, messagePostProcessor());
    }

    //处理待发送消息
    private MessagePostProcessor messagePostProcessor(){
        return  new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置有效期10S
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        };
    }

    /**
     * 主动查询接口
     * 先查询该订单的消息是否投递失败
     * 在查询数据库
     */
    @RequestMapping("/getOrder")
    public Object getOrder(String orderId) {
        OrderEntity orderEntity = orderMapper.getOrder(orderId);
        if (orderEntity == null) {
            return "消息正在异步的处理中";
        }
        return orderEntity;
    }
}
