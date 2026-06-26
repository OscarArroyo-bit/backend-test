package com.oscar.orderms.service;

import com.oscar.orderms.dto.CreateOrderRequest;
import com.oscar.orderms.dto.OrderResponse;
import com.oscar.orderms.entity.Order;
import com.oscar.orderms.enums.OrderStatus;
import com.oscar.orderms.exception.ResourceNotFoundException;
import com.oscar.orderms.kafka.dto.PaymentProcessedEvent;
import com.oscar.orderms.kafka.producer.OrderProducer;
import com.oscar.orderms.kafka.dto.OrderPlacedEvent;
import com.oscar.orderms.repository.OrderRepository;
import com.oscar.orderms.security.RSAService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;
    private final RSAService rsaService;

    public OrderService(OrderRepository orderRepository,
                        OrderProducer orderProducer,
                        RSAService rsaService) {

        this.orderRepository = orderRepository;
        this.orderProducer = orderProducer;
        this.rsaService = rsaService;
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
        String encryptedCard = rsaService.encrypt(savedOrder.getCardNumber());

        OrderPlacedEvent event = OrderPlacedEvent.builder()
                .orderId(savedOrder.getId())
                .productName(savedOrder.getProductName())
                .quantity(savedOrder.getQuantity())
                .price(savedOrder.getPrice())
                .cardNumber(encryptedCard)
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

    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
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

    public void updateOrderStatus(PaymentProcessedEvent event) {

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order with id " + event.getOrderId() + " not found"));

        if (event.isSuccess()) {
            order.setStatus(OrderStatus.PAGADO);
        } else {
            order.setStatus(OrderStatus.FALLO_PAGO);
        }

        orderRepository.save(order);

        logger.info("Order {} updated to status {}",
                order.getId(),
                order.getStatus());
    }
}
