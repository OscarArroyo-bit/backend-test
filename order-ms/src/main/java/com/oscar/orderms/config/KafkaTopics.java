package com.oscar.orderms.config;

/**
 * Centralizes the names of all Kafka topics used by the application.
 */
public class KafkaTopics {
    public static final String ORDER_PLACED = "order-placed";
    public static final String PAYMENT_PROCESSED = "payment-processed";

    private KafkaTopics(){}
}
