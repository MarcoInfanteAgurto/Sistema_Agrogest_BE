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
public class UsuarioCreadoConsumer {

    private final InMemoryNotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "usuario-creado", groupId = "notification-service-group")
    public void onUsuarioCreado(String eventJson) {
        log.info("📩 Evento recibido → usuario-creado | {}", eventJson);

        try {
            Map<String, Object> event = objectMapper.readValue(eventJson, Map.class);
            UUID usuarioId = UUID.fromString(event.get("userId").toString());
            String nombre = event.get("nombre") != null ? event.get("nombre").toString() : "Usuario";
            String email = event.get("email") != null ? event.get("email").toString() : "N/A";

            Notificacion notificacion = Notificacion.builder()
                    .id(UUID.randomUUID())
                    .usuarioId(usuarioId)
                    .tipo("Bienvenida")
                    .titulo("¡Bienvenido a AgroGest, " + nombre + "!")
                    .mensaje("Tu cuenta ha sido creada exitosamente. " +
                             "Comienza registrando tu primera parcela.")
                    .leida(false)
                    .prioridad("Media")
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationService.create(notificacion);
            log.info("✅ Notificación de bienvenida creada para: {}", email);
        } catch (Exception e) {
            log.error("❌ Error procesando evento usuario-creado: {}", e.getMessage());
        }
    }
}
