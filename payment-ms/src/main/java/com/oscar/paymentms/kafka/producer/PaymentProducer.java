package com.oscar.paymentms.kafka.producer;

import com.oscar.paymentms.config.KafkaTopics;
import com.oscar.paymentms.kafka.dto.PaymentProcessedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentProducer {

    private static final Logger logger =
            LoggerFactory.getLogger(PaymentProducer.class);

    private final KafkaTemplate<String, PaymentProcessedEvent> kafkaTemplate;

    public PaymentProducer(KafkaTemplate<String, PaymentProcessedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(PaymentProcessedEvent event) {

        logger.info("Publishing payment result for order {}", event.getOrderId());

        kafkaTemplate.send(
                KafkaTopics.PAYMENT_PROCESSED,
                event.getOrderId().toString(),
                event
        );
    }
}