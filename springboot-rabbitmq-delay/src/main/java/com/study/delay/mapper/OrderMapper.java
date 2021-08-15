package com.study.delay.mapper;

import com.study.delay.model.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper {
    @Insert("insert into orders(order_name,order_id,order_status) values (#{orderName},#{orderId} ,#{orderStatus})")
    int addOrder(Orders orderEntity);

    @Select("SELECT * from orders where order_id=#{orderId}")
    Orders getOrder(String orderId);

    @Update("update orders set order_status=#{orderStatus} where order_id=#{orderId}")
    Integer updateStatus(String orderId, Integer orderStatus);
}
