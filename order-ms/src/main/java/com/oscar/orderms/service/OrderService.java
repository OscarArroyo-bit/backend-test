package com.oscar.orderms.service;

import com.oscar.orderms.dto.CreateOrderRequest;
import com.oscar.orderms.dto.OrderResponse;
import com.oscar.orderms.entity.Order;
import com.oscar.orderms.enums.OrderStatus;
import com.oscar.orderms.exception.ResourceNotFoundException;
import com.oscar.orderms.kafka.OrderProducer;
import com.oscar.orderms.kafka.dto.OrderPlacedEvent;
import com.oscar.orderms.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;

    public OrderService(OrderRepository orderRepository, OrderProducer orderProducer){
        this.orderRepository = orderRepository;
        this.orderProducer = orderProducer;
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

        OrderPlacedEvent event = OrderPlacedEvent.builder()
                .orderId(savedOrder.getId())
                .productName(savedOrder.getProductName())
                .quantity(savedOrder.getQuantity())
                .price(savedOrder.getPrice())
                .cardNumber(savedOrder.getCardNumber())
                .build();

        orderProducer.send(event);

        return mapToResponse(savedOrder);
    }

    public OrderResponse getOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order with id " + id + " not found"));

        return mapToResponse(order);
    }

    private OrderResponse mapToResponse(Order order) {

        return OrderResponse.builder()
                .id(order.getId())
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .status(String.valueOf(order.getStatus()))
                .build();
    }
}
