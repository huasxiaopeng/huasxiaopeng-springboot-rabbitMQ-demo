package com.study.deadletter.model;

import lombok.Data;

@Data
public class OrderEntity {
    private int id;
    private String orderName;
    private String orderId;

    public OrderEntity(String orderName, String orderId) {
        this.orderName = orderName;
        this.orderId = orderId;
    }

    public OrderEntity() {

    }
}
