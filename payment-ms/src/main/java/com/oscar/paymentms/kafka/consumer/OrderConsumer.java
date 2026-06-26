package com.oscar.paymentms.kafka.consumer;

import com.oscar.paymentms.config.KafkaTopics;
import com.oscar.paymentms.kafka.dto.OrderPlacedEvent;
import com.oscar.paymentms.kafka.dto.PaymentProcessedEvent;
import com.oscar.paymentms.kafka.producer.PaymentProducer;
import com.oscar.paymentms.security.RSAService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class OrderConsumer {

    private static final Logger logger =
            LoggerFactory.getLogger(OrderConsumer.class);

    private final PaymentProducer paymentProducer;
    private final RSAService rsaService;

    public OrderConsumer(PaymentProducer paymentProducer,
                         RSAService rsaService) {

        this.paymentProducer = paymentProducer;
        this.rsaService = rsaService;
    }

    @KafkaListener(
            topics = KafkaTopics.ORDER_PLACED,
            groupId = "payment-group"
    )
    public void consume(OrderPlacedEvent orderPlacedEvent) {

        String cardNumber = rsaService.decrypt(orderPlacedEvent.getCardNumber());
        logger.info("==================================");
        logger.info("ORDER RECEIVED");
        logger.info("Order Id: {}", orderPlacedEvent.getOrderId());
        logger.info("Product: {}", orderPlacedEvent.getProductName());
        logger.info("Quantity: {}", orderPlacedEvent.getQuantity());
        logger.info("Price: {}", orderPlacedEvent.getPrice());
        logger.info("RSA Card Number: {}", orderPlacedEvent.getCardNumber());
        logger.info("Card Number: {}", cardNumber);
        logger.info("==================================");


        Random random = new Random();
        boolean success = random.nextBoolean();

        PaymentProcessedEvent event = PaymentProcessedEvent.builder()
                .orderId(orderPlacedEvent.getOrderId())
                .success(success)
                .build();

        paymentProducer.send(event);

        logger.info("Payment result published for order {}",
                orderPlacedEvent.getOrderId());
    }
}