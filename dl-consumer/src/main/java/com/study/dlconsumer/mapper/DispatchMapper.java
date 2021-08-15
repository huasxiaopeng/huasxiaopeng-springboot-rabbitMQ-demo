package com.study.dlconsumer.mapper;

import com.study.dlconsumer.model.Dispatch;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DispatchMapper {

    /**
     * 新增派单任务
     */
    @Insert("INSERT into dispatch(order_id,user_id) values (#{orderId},#{userId})")
    int insertDistribute(Dispatch distributeEntity);

}
