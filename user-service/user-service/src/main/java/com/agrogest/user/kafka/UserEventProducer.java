package com.agrogest.user.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    private static final String TOPIC = "usuario-creado";

    public void publishUserCreated(UUID userId, String email, String nombre) {
        UserCreatedEvent event = new UserCreatedEvent(userId, email, nombre);
        kafkaTemplate.send(TOPIC, userId.toString(), event);
        log.info("Evento publicado -> topic: {} | usuario: {}", TOPIC, email);
    }
}
