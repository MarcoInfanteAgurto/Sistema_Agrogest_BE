package com.agrogest.parcel.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParcelEventProducer {

    private final KafkaTemplate<String, ParcelCreatedEvent> kafkaTemplate;

    private static final String TOPIC = "parcela-creada";

    public void publishParcelCreated(UUID parcelId, UUID usuarioId, String nombre, Double latitud, Double longitud) {
        ParcelCreatedEvent event = new ParcelCreatedEvent(parcelId, usuarioId, nombre, latitud, longitud);
        kafkaTemplate.send(TOPIC, parcelId.toString(), event);
        log.info("Evento publicado → topic: {} | parcela: {}", TOPIC, nombre);
    }
}
