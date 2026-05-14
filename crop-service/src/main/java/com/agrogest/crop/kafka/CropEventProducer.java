package com.agrogest.crop.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CropEventProducer {

    private final KafkaTemplate<String, SiembraCreatedEvent> kafkaTemplate;

    private static final String TOPIC = "siembra-creada";

    public void publishSiembraCreated(UUID siembraId, UUID parcelaId,
                                       UUID cultivoId, UUID usuarioId) {
        SiembraCreatedEvent event = new SiembraCreatedEvent(
                siembraId, parcelaId, cultivoId, usuarioId);
        kafkaTemplate.send(TOPIC, siembraId.toString(), event);
        log.info("✅ Evento publicado → topic: {} | siembra: {}", TOPIC, siembraId);
    }
}
