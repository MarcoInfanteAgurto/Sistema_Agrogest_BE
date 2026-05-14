package com.agrogest.notification.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component("kafkaErrorHandler")
public class KafkaErrorHandler implements KafkaListenerErrorHandler {

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        log.warn("⚠️ Kafka error (non-fatal): {}", exception.getMessage());
        return null;
    }

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, org.apache.kafka.clients.consumer.Consumer<?, ?> consumer) {
        log.warn("⚠️ Kafka consumer error (non-fatal): {}", exception.getMessage());
        return null;
    }
}
