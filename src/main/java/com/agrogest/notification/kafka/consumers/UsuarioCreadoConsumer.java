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
        topics = "notifications", 
        groupId = "notification-service-group",
        errorHandler = "kafkaErrorHandler"
    )
    public void onNotificationReceived(String eventJson) {
        log.info("📩 Evento recibido → notifications | {}", eventJson);

        try {
            Map<String, Object> event = objectMapper.readValue(eventJson, Map.class);
            
            String nombre = event.get("nombre") != null ? event.get("nombre").toString() : "Usuario";
            String titulo = event.get("titulo") != null ? event.get("titulo").toString() : "Notificación del Sistema";
            String mensaje = event.get("mensaje") != null ? event.get("mensaje").toString() : "Detalle de actividad en AgroGest para: " + nombre;
            String prioridad = event.get("prioridad") != null ? event.get("prioridad").toString() : "Baja";
            String tipo = event.get("tipo") != null ? event.get("tipo").toString() : "Sistema";
            
            String usuarioIdStr = event.get("usuarioId") != null ? event.get("usuarioId").toString() : "dd529113-9a0c-47b3-898c-11442bd42926";
            UUID miUsuarioLogueadoId = UUID.fromString(usuarioIdStr);

            Notificacion notificacion = Notificacion.builder()
                    .usuarioId(miUsuarioLogueadoId)
                    .tipo(tipo)
                    .titulo(titulo)
                    .mensaje(mensaje)
                    .leida(false)
                    .prioridad(prioridad)
                    .createdAt(LocalDateTime.now())
                    .build();

            Notificacion guardada = notificationRepository.save(notificacion);
            log.info("✅ Notificación guardada en DB para tu panel historial");

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

            if (!NotificacionController.emitters.isEmpty()) {
                NotificacionController.emitters.forEach(emitter -> {
                    try {
                        emitter.send(response);
                    } catch (Exception e) {
                        NotificacionController.emitters.remove(emitter);
                    }
                });
            }

        } catch (Exception e) {
            log.error("❌ Error procesando evento notifications: {}", e.getMessage());
        }
    }
}