package com.oscar.orderms.controller;

import com.oscar.orderms.dto.CreateOrderRequest;
import com.oscar.orderms.dto.OrderResponse;
import com.oscar.orderms.entity.Order;
import com.oscar.orderms.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request){
        return orderService.createOrder(request);
    }
}
