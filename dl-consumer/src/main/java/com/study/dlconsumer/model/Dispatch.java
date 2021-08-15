package com.study.dlconsumer.model;

import lombok.Data;

@Data
public class Dispatch {

    private Long id;
    // 订单号
    private String orderId;

    // 派单id
    private Long userId;

    public Dispatch(String orderId, Long userId) {
        this.orderId = orderId;
        this.userId = userId;
    }
}
