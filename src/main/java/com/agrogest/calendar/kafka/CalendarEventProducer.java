package com.agrogest.calendar.kafka;

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
public class CalendarEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "calendario-creado";

    public void publishCalendarioCreado(UUID eventoId, String titulo, UUID usuarioId) {
        try {
            Map<String, Object> evento = new HashMap<>();
            evento.put("eventoId", eventoId.toString());
            evento.put("titulo", titulo);
            evento.put("usuarioId", usuarioId.toString());
            
            String eventoJson = objectMapper.writeValueAsString(evento);
            kafkaTemplate.send(TOPIC, eventoId.toString(), eventoJson);
            log.info("✅ Evento calendario-creado publicado - Evento: {} | Título: {}", eventoId, titulo);
        } catch (Exception e) {
            log.error("❌ Error publicando evento calendario-creado: {}", e.getMessage(), e);
        }
    }
}
