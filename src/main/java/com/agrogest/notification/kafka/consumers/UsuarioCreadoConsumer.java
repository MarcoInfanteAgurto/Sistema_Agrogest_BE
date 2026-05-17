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
        
        String nombre = event.get("nombre") != null ? event.get("nombre").toString() : "Usuario";
        String email = event.get("email") != null ? event.get("email").toString() : "N/A";

        // 🚀 SOLUCIÓN: Guardamos la notificación vinculada a TU ID de pruebas en Neon
        UUID miUsuarioLogueadoId = UUID.fromString("2226fb12-dc4b-4067-91fa-f5b3cc4fed5a");

        Notificacion notificacion = Notificacion.builder()
                .usuarioId(miUsuarioLogueadoId) // Al guardarse con tu ID, el F5 la encontrará siempre
                .tipo("Sistema")
                .titulo("Usuario Registrado")
                .mensaje("Se ha creado con éxito al usuario: " + nombre)
                .leida(false)
                .prioridad("Baja")
                .createdAt(LocalDateTime.now())
                .build();

        Notificacion guardada = notificationRepository.save(notificacion);
        log.info("✅ Notificación guardada en DB para tu panel historial");

        // 2. Transmisión SSE (EXACTAMENTE IGUAL a como lo tenías para que reaccione la campana en vivo)
        NotificacionResponse response = NotificacionResponse.builder()
                .id(guardada.getId())
                .usuarioId(null) 
                .tipo("Sistema")
                .titulo("Usuario Registrado")
                .mensaje("Se ha creado con éxito al usuario: " + nombre)
                .leida(false)
                .prioridad("Baja")
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
        log.error("❌ Error procesando evento usuario-creado: {}", e.getMessage());
    }
}
}