package com.agrogest.notification.kafka.consumers;

import com.agrogest.notification.service.KafkaNotificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalendarioCreadoConsumer {

    private final KafkaNotificationService kafkaNotificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "calendario-creado", groupId = "notification-group")
    public void onCalendarioCreado(String message) {
        try {
            JsonNode json = objectMapper.readTree(message);
            UUID usuarioId = UUID.fromString(json.get("usuarioId").asText());
            String titulo = json.get("titulo").asText();
            String descripcion = json.has("descripcion") ? json.get("descripcion").asText() : "Evento de calendario creado";
            
            kafkaNotificationService.procesarYEnviar(
                usuarioId,
                com.agrogest.notification.model.TipoNotificacion.SISTEMA,
                titulo,
                descripcion,
                com.agrogest.notification.model.Prioridad.MEDIA,
                null
            );
            
            log.info("✅ Notificación procesada para calendario-creado - Usuario: {}", usuarioId);
        } catch (Exception e) {
            log.error("❌ Error procesando evento calendario-creado: {}", e.getMessage(), e);
        }
    }
}
