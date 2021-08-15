package com.study.delay.consumer;

import com.study.delay.mapper.OrderMapper;
import com.study.delay.model.Orders;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 死信队列
 */
@Component
public class OrderDlxConsumer {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 监听死信队列
     * 走逻辑判断，尚未支付且超过过期时间的订单号设置为失效订单
     */
    @RabbitListener(queues = "study_order_dlx_queue")
    public void orderConsumer(String orderId) {
        System.out.println("死信队列获取消息:" + orderId);
        if (StringUtils.isEmpty(orderId)) {
            return;
        }
        //根据id查询
        Orders orderEntity = orderMapper.getOrder(orderId);
        if (null == orderEntity) {
            return;
        }
        //获取状态
        Integer orderStatus = orderEntity.getOrderStatus();
        //判断未支付 , 关闭订单
        if (0 == orderStatus) {
            orderMapper.updateStatus(orderId, 2);
            //库存返还
        }
    }
}
