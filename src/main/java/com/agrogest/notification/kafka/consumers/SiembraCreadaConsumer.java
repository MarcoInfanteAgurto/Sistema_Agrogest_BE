package com.agrogest.notification.kafka.consumers;

import com.agrogest.notification.model.Notificacion;
import com.agrogest.notification.service.InMemoryNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SiembraCreadaConsumer {

    private final InMemoryNotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "siembra-creada", groupId = "notification-service-group")
    public void onSiembraCreada(String eventJson) {
        log.info("📩 Evento recibido → siembra-creada | {}", eventJson);

        try {
            Map<String, Object> event = objectMapper.readValue(eventJson, Map.class);
            UUID usuarioId = UUID.fromString(event.get("usuarioId").toString());
            UUID siembraId = UUID.fromString(event.get("id").toString());
            UUID parcelaId = event.get("parcelaId") != null ? UUID.fromString(event.get("parcelaId").toString()) : null;

            Notificacion notificacion = Notificacion.builder()
                    .id(UUID.randomUUID())
                    .usuarioId(usuarioId)
                    .tipo("Siembra")
                    .titulo("Nueva siembra iniciada")
                    .mensaje("Se ha registrado una nueva siembra en tu parcela. " +
                             "Recuerda configurar el riego y monitorear el progreso.")
                    .leida(false)
                    .prioridad("Alta")
                    .parcelaId(parcelaId)
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationService.create(notificacion);
            log.info("✅ Notificación de siembra creada para usuario: {}", usuarioId);
        } catch (Exception e) {
            log.error("❌ Error procesando evento siembra-creada: {}", e.getMessage());
        }
    }
}
