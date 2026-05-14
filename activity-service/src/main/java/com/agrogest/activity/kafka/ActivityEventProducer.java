package com.agrogest.activity.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityEventProducer {

    private final KafkaTemplate<String, ActividadCreatedEvent> kafkaTemplate;

    private static final String TOPIC = "actividad-creada";

    public void publishActividadCreated(UUID actividadId, UUID siembraId,
                                         UUID usuarioId, String tipo) {
        ActividadCreatedEvent event = new ActividadCreatedEvent(
                actividadId, siembraId, usuarioId, tipo);
        kafkaTemplate.send(TOPIC, actividadId.toString(), event);
        log.info("✅ Evento publicado → topic: {} | actividad: {}", TOPIC, actividadId);
    }
}
