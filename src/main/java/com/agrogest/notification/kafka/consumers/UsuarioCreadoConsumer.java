package com.agrogest.notification.kafka.consumers;

import com.agrogest.notification.controller.NotificacionController;
import com.agrogest.notification.dto.NotificacionResponse;
import com.agrogest.notification.model.Notificacion;
import com.agrogest.notification.repository.NotificacionRepository;
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

    private final NotificacionRepository notificationRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "usuario-creado", 
        groupId = "notification-service-group",
        errorHandler = "kafkaErrorHandler"
    )
    public void onUsuarioCreado(String eventJson) {
        log.info("📩 Evento recibido → usuario-creado | {}", eventJson);

        try {
            Map<String, Object> event = objectMapper.readValue(eventJson, Map.class);
            // Nota: Se usa "userId" del mapa según el JSON original
            UUID usuarioId = UUID.fromString(event.get("userId").toString());
            String nombre = event.get("nombre") != null ? event.get("nombre").toString() : "Usuario";
            String email = event.get("email") != null ? event.get("email").toString() : "N/A";

            // 1. Construcción del objeto de bienvenida
            Notificacion notificacion = Notificacion.builder()
                    .usuarioId(usuarioId)
                    .tipo("Bienvenida")
                    .titulo("¡Bienvenido a AgroGest, " + nombre + "!")
                    .mensaje("Tu cuenta ha sido creada exitosamente. Comienza registrando tu primera parcela.")
                    .leida(false)
                    .prioridad("Media")
                    .createdAt(LocalDateTime.now())
                    .build();

            // 2. Guardado en Neon
            Notificacion guardada = notificationRepository.save(notificacion);
            log.info("✅ Notificación de bienvenida guardada en DB para: {}", email);

            // 3. Notificación en tiempo real vía SSE
            NotificacionResponse response = NotificacionResponse.builder()
                    .id(guardada.getId())
                    .usuarioId(guardada.getUsuarioId())
                    .tipo(guardada.getTipo())
                    .titulo(guardada.getTitulo())
                    .mensaje(guardada.getMensaje())
                    .leida(guardada.getLeida())
                    .prioridad(guardada.getPrioridad())
                    .createdAt(guardada.getCreatedAt())
                    .build();

            NotificacionController.emitters.forEach(emitter -> {
                try {
                    emitter.send(response);
                } catch (Exception e) {
                    NotificacionController.emitters.remove(emitter);
                }
            });

        } catch (Exception e) {
            log.error("❌ Error procesando evento usuario-creado: {}", e.getMessage());
        }
    }
}