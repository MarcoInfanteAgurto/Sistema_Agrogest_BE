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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActividadCreadaConsumer {

    private final NotificacionRepository notificationRepository; 
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
            String tipoActividad = event.get("tipo") != null ? event.get("tipo").toString() : "Actividad";

            // 1. Construcción del objeto
            Notificacion notificacion = Notificacion.builder()
                    .usuarioId(usuarioId)
                    .tipo("Actividad")
                    .titulo("Nueva actividad registrada")
                    .mensaje("Se ha registrado una nueva actividad: " + tipoActividad + ". Revisa el calendario para más detalles.")
                    .leida(false)
                    .prioridad("Media")
                    .createdAt(LocalDateTime.now())
                    .build();

            // 2. Guardado en Neon (PostgreSQL)
            Notificacion guardada = notificationRepository.save(notificacion); 
            log.info("✅ Notificación guardada en Neon para usuario: {}", usuarioId);

            // 3. ¡TIEMPO REAL! Enviar a Angular automáticamente
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
        emitter.send(SseEmitter.event()
                .name("message")
                .data(response));
    } catch (Exception e) {
        log.warn("Eliminando emisor fallido");
        NotificacionController.emitters.remove(emitter);
    }
});

        } catch (Exception e) {
            log.error("❌ Error procesando evento actividad-creada: {}", e.getMessage());
        }
    }
}