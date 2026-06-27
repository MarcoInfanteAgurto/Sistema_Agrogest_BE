package com.agrogest.notification.kafka.consumers;

import com.agrogest.notification.controller.NotificacionController;
import com.agrogest.notification.dto.NotificacionResponse;
import com.agrogest.notification.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CultivoCreadoConsumer {

    private final NotificacionService notificacionService;

    @KafkaListener(
        topics = "siembra-creada", 
        groupId = "notification-service-group"
    )
    public void consume(String message) {
        log.info("📩 Evento recibido desde Kafka → siembra-creada | {}", message);
        
        try {
            NotificacionResponse notif = notificacionService.procesarYGuardar(message);

            if (notif != null && notif.getUsuarioId() != null) {
                SseEmitter emitter = NotificacionController.emitters.get(notif.getUsuarioId());
                if (emitter != null) {
                    try {   
                        log.info("🚀 Enviando notificación en tiempo real vía SSE al usuario: {}", notif.getUsuarioId());
                        emitter.send(SseEmitter.event().name("nueva-notificacion").data(notif));
                    } catch (IOException e) {
                        log.warn("⚠️ Emitter obsoleto detectado para el usuario: {}. Removiendo...", notif.getUsuarioId());
                        NotificacionController.emitters.remove(notif.getUsuarioId());
                    }
                } else {
                    log.warn("ℹ️ No hay un canal SSE activo (emitter) para el usuario: {}", notif.getUsuarioId());
                }
            }
        } catch (Exception e) {
            log.error("❌ Error crítico procesando la notificación de cultivo: {}", e.getMessage(), e);
        }
    }
}