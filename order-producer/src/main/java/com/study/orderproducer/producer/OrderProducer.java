package com.study.orderproducer.producer;

import com.alibaba.fastjson.JSONObject;
import com.study.orderproducer.mapper.OrderMapper;
import com.study.orderproducer.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
@Slf4j
public class OrderProducer implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional
    public String sendOrder() {
        // 1.先创建订单信息
        String orderId = System.currentTimeMillis() + "";
        Order orderEntity = createOrder(orderId);
        // 2.添加到数据库中
        int result = orderMapper.addOrder(orderEntity);
        if (result <= 0) {
            return null;
        }
        // 3.订单数据库插入成功的情况下, 使用MQ异步发送派单信息
        String msgJson = JSONObject.toJSONString(orderEntity);
        System.out.println("<<<<<<<1线程名称是>>>>>:"+Thread.currentThread().getName());
        sendMsg(msgJson);
        /**
         * 模拟创建订单后续流程出现问题，此时，消息已经发送给了mq
         * 所以订单消息只能在消费者处进行补单操作
         *
         */

//        int i=1/0;
        return orderId;
    }

    //发送消息
    public void sendMsg(String msgJson) {
        // 设置生产者消息确认机制
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(this);
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(msgJson);
        String orderExchange = "order_exchange_name"; //订单交换机
        String orderRoutingKey = "orderRoutingKey";  //订单路由key
        rabbitTemplate.convertAndSend(orderExchange, orderRoutingKey, msgJson, correlationData);
    }

    //创建订单信息
    public Order createOrder(String orderId) {
        Order orderEntity = new Order();
        orderEntity.setName("咕泡教育-Java架构师");
        orderEntity.setOrderCreatetime(new Date());
        // 价格是300元
        orderEntity.setOrderMoney(300d);
        // 状态为 未支付
        orderEntity.setOrderState(0);
        Long commodityId = 30L;
        // 商品id
        orderEntity.setCommodityId(commodityId);
        orderEntity.setOrderId(orderId);
        return orderEntity;
    }

    /**
     * 实现ConfirmCallback接口 重写confirm方法
     * @param correlationData 投递失败回调消息
     * @param ack true 投递到MQ成功  false投递消息失败
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String msg = correlationData.getId();
        if(!ack){
            log.info("<<<往MQ投递消息失败>>>>: {}" , msg);
            //采用递归重试
            sendMsg(msg);
            return;
        }
        log.info("<<<往MQ投递消息成功>>>>: {}" , msg);
        // 生产者投递多次还是is的情况下应该 人工记录
    }
}
