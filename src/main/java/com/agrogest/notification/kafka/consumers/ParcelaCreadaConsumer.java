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
public class ParcelaCreadaConsumer {

    private final InMemoryNotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "parcela-creada", groupId = "notification-service-group")
    public void onParcelaCreada(String eventJson) {
        log.info("📩 Evento recibido → parcela-creada | {}", eventJson);

        try {
            Map<String, Object> event = objectMapper.readValue(eventJson, Map.class);
            UUID usuarioId = UUID.fromString(event.get("usuarioId").toString());
            UUID parcelaId = UUID.fromString(event.get("id").toString());
            String nombre = event.get("nombre") != null ? event.get("nombre").toString() : "Parcela";

            Notificacion notificacion = Notificacion.builder()
                    .id(UUID.randomUUID())
                    .usuarioId(usuarioId)
                    .tipo("Parcela")
                    .titulo("Parcela registrada exitosamente")
                    .mensaje("La parcela '" + nombre + "' " +
                             "ha sido registrada. Ya puedes iniciar una siembra.")
                    .leida(false)
                    .prioridad("Baja")
                    .parcelaId(parcelaId)
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationService.create(notificacion);
            log.info("✅ Notificación de parcela creada: {}", nombre);
        } catch (Exception e) {
            log.error("❌ Error procesando evento parcela-creada: {}", e.getMessage());
        }
    }
}
