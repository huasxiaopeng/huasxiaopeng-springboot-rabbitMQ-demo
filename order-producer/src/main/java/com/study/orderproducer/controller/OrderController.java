package com.study.orderproducer.controller;

import com.study.orderproducer.producer.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderController {
    @Autowired
    private OrderProducer orderProducer;

    @RequestMapping("/sendOrder")
    public String sendOrder() {
        return orderProducer.sendOrder();
    }
}
