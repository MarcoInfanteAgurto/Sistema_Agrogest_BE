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
public class ParcelaCreadaConsumer {

    private final NotificacionRepository notificationRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "parcela-creada", 
        groupId = "notification-service-group",
        errorHandler = "kafkaErrorHandler"
    )
    public void onParcelaCreada(String eventJson) {
        log.info("📩 Evento recibido → parcela-creada | {}", eventJson);

        try {
            Map<String, Object> event = objectMapper.readValue(eventJson, Map.class);
            UUID usuarioId = UUID.fromString(event.get("usuarioId").toString());
            String nombreParcela = event.get("nombre") != null ? event.get("nombre").toString() : "Nueva Parcela";

            // 1. Construcción del objeto
            Notificacion notificacion = Notificacion.builder()
                    .usuarioId(usuarioId)
                    .tipo("Parcela")
                    .titulo("Nueva parcela creada")
                    .mensaje("Se ha registrado con éxito la parcela: " + nombreParcela)
                    .leida(false)
                    .prioridad("Baja")
                    .createdAt(LocalDateTime.now())
                    .build();

            // 2. Guardado en Neon
            Notificacion guardada = notificationRepository.save(notificacion);
            log.info("✅ Notificación de parcela guardada para usuario: {}", usuarioId);

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
                    emitter.send(response);
                } catch (Exception e) {
                    NotificacionController.emitters.remove(emitter);
                }
            });

        } catch (Exception e) {
            log.error("❌ Error procesando evento parcela-creada: {}", e.getMessage());
        }
    }
}