package com.oscar.orderms.service;

import com.oscar.orderms.dto.CreateOrderRequest;
import com.oscar.orderms.dto.OrderResponse;
import com.oscar.orderms.entity.Order;
import com.oscar.orderms.enums.OrderStatus;
import com.oscar.orderms.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(CreateOrderRequest request) {

        Order order = Order.builder()
                .productName(request.getProductName())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .cardNumber(request.getCardNumber())
                .status(OrderStatus.PENDIENTE)
                .build();

        Order savedOrder = orderRepository.save(order);

        return OrderResponse.builder()
                .id(savedOrder.getId())
                .productName(savedOrder.getProductName())
                .quantity(savedOrder.getQuantity())
                .price(savedOrder.getPrice())
                .status(String.valueOf(savedOrder.getStatus()))
                .build();
    }
}
