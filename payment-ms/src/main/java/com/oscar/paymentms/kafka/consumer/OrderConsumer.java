package com.oscar.paymentms.kafka.consumer;

import com.oscar.paymentms.config.KafkaTopics;
import com.oscar.paymentms.kafka.dto.OrderPlacedEvent;
import com.oscar.paymentms.kafka.dto.PaymentProcessedEvent;
import com.oscar.paymentms.kafka.producer.PaymentProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private static final Logger logger =
            LoggerFactory.getLogger(OrderConsumer.class);

    private final PaymentProducer paymentProducer;

    public OrderConsumer(PaymentProducer paymentProducer) {
        this.paymentProducer = paymentProducer;
    }

    @KafkaListener(
            topics = KafkaTopics.ORDER_PLACED,
            groupId = "payment-group"
    )
    public void consume(OrderPlacedEvent orderPlacedEvent) {

        logger.info("==================================");
        logger.info("ORDER RECEIVED");
        logger.info("Order Id: {}", orderPlacedEvent.getOrderId());
        logger.info("Product: {}", orderPlacedEvent.getProductName());
        logger.info("Quantity: {}", orderPlacedEvent.getQuantity());
        logger.info("Price: {}", orderPlacedEvent.getPrice());
        logger.info("==================================");

        boolean success = true;

        PaymentProcessedEvent event = PaymentProcessedEvent.builder()
                .orderId(orderPlacedEvent.getOrderId())
                .success(success)
                .build();

        paymentProducer.send(event);

        logger.info("Payment result published for order {}",
                orderPlacedEvent.getOrderId());
    }
}