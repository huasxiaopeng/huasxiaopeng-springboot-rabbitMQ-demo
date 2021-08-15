package com.study.deadletter.mapper;

import com.study.deadletter.model.OrderEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {
    @Insert("insert into orders(order_name,order_id) values (#{orderName},#{orderId})")
    int addOrder(OrderEntity orderEntity);

    @Select("SELECT * from orders where order_id=#{orderId} ")
    OrderEntity getOrder(String orderId);
}
