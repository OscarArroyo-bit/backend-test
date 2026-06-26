package com.oscar.orderms.kafka.consumer;

import com.oscar.orderms.config.KafkaTopics;
import com.oscar.orderms.kafka.dto.PaymentProcessedEvent;
import com.oscar.orderms.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Consumes payment result events and updates the
 * corresponding order status.
 */
@Service
public class PaymentProcessedConsumer {
    private static final Logger logger = LoggerFactory.getLogger(PaymentProcessedConsumer.class);

    private final OrderService orderService;

    public PaymentProcessedConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(
            topics = KafkaTopics.PAYMENT_PROCESSED,
            groupId = "order-group"
    )
    public void consume(PaymentProcessedEvent event) {

        logger.info("==================================");
        logger.info("PAYMENT RESULT RECEIVED");
        logger.info("Order Id: {}", event.getOrderId());
        if (event.isSuccess()) {
            logger.info("Payment approved for order {}", event.getOrderId());
        } else {
            logger.info("Payment rejected for order {}", event.getOrderId());
        }
        logger.info("==================================");

        orderService.updateOrderStatus(event);

        logger.info("Order {} updated successfully", event.getOrderId());
    }
}
