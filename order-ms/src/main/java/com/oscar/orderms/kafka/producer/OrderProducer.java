package com.oscar.orderms.kafka.producer;

import com.oscar.orderms.config.KafkaTopics;
import com.oscar.orderms.kafka.dto.OrderPlacedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderProducer {

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    private static final Logger logger =
            LoggerFactory.getLogger(OrderProducer.class);

    public OrderProducer(KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(OrderPlacedEvent event){
        logger.info("Sending order {} to Kafka", event.getOrderId());
        kafkaTemplate.send(KafkaTopics.ORDER_PLACED, event.getOrderId().toString(), event);
    }
}
