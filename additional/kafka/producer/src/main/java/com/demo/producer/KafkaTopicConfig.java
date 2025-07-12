package com.demo.producer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
// Creating Kafka Topics Through Configuration.

    @Bean
    public NewTopic createMyTopic() {
        return new NewTopic("my-new-topic-1", 3, (short) 1);
    }
}
