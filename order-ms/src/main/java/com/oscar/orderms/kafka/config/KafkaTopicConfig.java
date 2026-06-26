package com.oscar.orderms.kafka.config;

import com.oscar.orderms.config.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderPlacedTopic() {
        return new NewTopic(KafkaTopics.ORDER_PLACED, 1, (short) 1);
    }
}
