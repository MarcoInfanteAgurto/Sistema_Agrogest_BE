package com.agrogest.crop.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CultivoEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "cultivo-creado";

    public void publishCultivoCreated(UUID cultivoId, String nombreCultivo) {
        try {
            Map<String, Object> evento = new HashMap<>();
            evento.put("cultivoId", cultivoId.toString());
            evento.put("nombreCultivo", nombreCultivo);
            
            String eventoJson = objectMapper.writeValueAsString(evento);
            kafkaTemplate.send(TOPIC, cultivoId.toString(), eventoJson);
            log.info("✅ Evento cultivo-creado publicado - Cultivo: {} | Nombre: {}", cultivoId, nombreCultivo);
        } catch (Exception e) {
            log.error("❌ Error publicando evento cultivo-creado: {}", e.getMessage(), e);
        }
    }
}
