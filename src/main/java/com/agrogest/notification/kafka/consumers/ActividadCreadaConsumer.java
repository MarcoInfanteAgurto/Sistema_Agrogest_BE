package com.agrogest.notification.kafka.consumers;

import com.agrogest.notification.model.Notificacion;
import com.agrogest.notification.service.InMemoryNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActividadCreadaConsumer {

    private final InMemoryNotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "actividad-creada", 
        groupId = "notification-service-group",
        errorHandler = "kafkaErrorHandler"
    )
    public void onActividadCreada(String eventJson) {
        log.info("📩 Evento recibido → actividad-creada | {}", eventJson);

        try {
            Map<String, Object> event = objectMapper.readValue(eventJson, Map.class);
            UUID usuarioId = UUID.fromString(event.get("usuarioId").toString());
            UUID actividadId = UUID.fromString(event.get("actividadId").toString());
            UUID siembraId = event.get("siembraId") != null ? UUID.fromString(event.get("siembraId").toString()) : null;
            String tipo = event.get("tipo") != null ? event.get("tipo").toString() : "Actividad";

            Notificacion notificacion = Notificacion.builder()
                    .id(UUID.randomUUID())
                    .usuarioId(usuarioId)
                    .tipo("Actividad")
                    .titulo("Nueva actividad registrada")
                    .mensaje("Se ha registrado una nueva actividad: " + tipo +
                             ". Revisa el calendario para más detalles.")
                    .leida(false)
                    .prioridad("Media")
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationService.create(notificacion);
            log.info("✅ Notificación de actividad creada para usuario: {}", usuarioId);
        } catch (Exception e) {
            log.error("❌ Error procesando evento actividad-creada: {}", e.getMessage());
        }
    }
}
