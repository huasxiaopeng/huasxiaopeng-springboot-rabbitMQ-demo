package com.study.orderproducer.mapper;

import com.study.orderproducer.model.Order;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderMapper {

    @Insert(value = "INSERT INTO `order`(name,order_create_time,order_money,order_state,commodity_id,order_id) VALUES (#{name}, #{orderCreatetime}, #{orderMoney}, #{orderState}, #{commodityId},#{orderId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int addOrder(Order orderEntity);


    @Select("SELECT * from order where order_id=#{orderId};")
    public Order findOrderId(@Param("orderId") String orderId);

}
