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
public class SiembraCreadaConsumer {

    private final NotificacionRepository notificationRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "siembra-creada", 
        groupId = "notification-service-group",
        errorHandler = "kafkaErrorHandler"
    )
    public void onSiembraCreada(String eventJson) {
        log.info("📩 Evento recibido → siembra-creada | {}", eventJson);

        try {
            Map<String, Object> event = objectMapper.readValue(eventJson, Map.class);
            UUID usuarioId = UUID.fromString(event.get("usuarioId").toString());
            String nombreSiembra = event.get("nombre") != null ? event.get("nombre").toString() : "Nueva Siembra";

            //Construcción del objeto (Dejamos que la DB genere el ID o se asigne si es necesario)
            Notificacion notificacion = Notificacion.builder()
                    .usuarioId(usuarioId)
                    .tipo("Siembra")
                    .titulo("Nueva siembra registrada")
                    .mensaje("Se ha iniciado una nueva siembra: " + nombreSiembra)
                    .leida(false)
                    .prioridad("Alta")
                    .createdAt(LocalDateTime.now())
                    .build();

            //Guardado persistente en Neon
            Notificacion guardada = notificationRepository.save(notificacion);
            log.info("✅ Notificación de siembra guardada para usuario: {}", usuarioId);

            //Notificación en tiempo real vía SSE
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
            log.error("❌ Error procesando evento siembra-creada: {}", e.getMessage());
        }
    }
}