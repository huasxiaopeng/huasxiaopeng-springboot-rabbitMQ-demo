package com.study.delay.model;

import lombok.Data;

@Data
public class Orders {
    private int id;
    private String orderName;
    private String orderId;
    private Integer orderStatus;

    public Orders(String orderName, String orderId) {
        this.orderName = orderName;
        this.orderId = orderId;
    }

    public Orders(String orderName, String orderId, Integer orderStatus) {
        this.orderName = orderName;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public Orders() {

    }
}
