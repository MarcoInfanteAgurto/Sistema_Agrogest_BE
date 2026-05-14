package com.agrogest.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableKafka
public class KafkaConfig {
    
    public KafkaConfig() {
        log.info("🔧 Kafka configuration loaded - consumers will attempt to connect");
    }
}
