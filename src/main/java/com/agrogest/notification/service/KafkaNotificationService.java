package com.agrogest.notification.service;

import com.agrogest.notification.dto.NotificacionResponse;
import com.agrogest.notification.model.Notificacion;
import com.agrogest.notification.model.Prioridad;
import com.agrogest.notification.model.TipoNotificacion;
import com.agrogest.notification.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaNotificationService {

    private final NotificacionRepository notificationRepository;

    public NotificacionResponse crearNotificacion(
            UUID usuarioId,
            TipoNotificacion tipo,
            String titulo,
            String mensaje,
            Prioridad prioridad,
            UUID parcelaId
    ) {
        Notificacion notificacion = Notificacion.builder()
                .usuarioId(usuarioId)
                .tipo(tipo.name())
                .titulo(titulo)
                .mensaje(mensaje)
                .leida(false)
                .prioridad(prioridad.name())
                .parcelaId(parcelaId)
                .createdAt(LocalDateTime.now())
                .build();

        Notificacion guardada = notificationRepository.save(notificacion);
        log.info("✅ Notificación guardada en DB - Tipo: {}, Usuario: {}", tipo, usuarioId);

        return NotificacionResponse.builder()
                .id(guardada.getId())
                .usuarioId(guardada.getUsuarioId())
                .tipo(guardada.getTipo())
                .titulo(guardada.getTitulo())
                .mensaje(guardada.getMensaje())
                .leida(guardada.getLeida())
                .prioridad(guardada.getPrioridad())
                .parcelaId(guardada.getParcelaId() != null ? guardada.getParcelaId().toString() : null)
                .createdAt(guardada.getCreatedAt())
                .build();
    }

    public void enviarPorSSE(NotificacionResponse response) {
        if (com.agrogest.notification.controller.NotificacionController.emitters.isEmpty()) {
            log.warn("⚠️ No hay clientes SSE conectados");
            return;
        }

        com.agrogest.notification.controller.NotificacionController.emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("notificacion")
                        .data(response, MediaType.APPLICATION_JSON));
            } catch (Exception e) {
                log.warn("❌ Error enviando SSE, eliminando emisor fallido: {}", e.getMessage());
                com.agrogest.notification.controller.NotificacionController.emitters.remove(emitter);
            }
        });
    }

    public NotificacionResponse procesarYEnviar(
            UUID usuarioId,
            TipoNotificacion tipo,
            String titulo,
            String mensaje,
            Prioridad prioridad,
            UUID parcelaId
    ) {
        try {
            NotificacionResponse response = crearNotificacion(
                    usuarioId, tipo, titulo, mensaje, prioridad, parcelaId
            );
            enviarPorSSE(response);
            return response;
        } catch (Exception e) {
            log.error("❌ Error procesando notificación Kafka: {}", e.getMessage(), e);
            throw new RuntimeException("Error procesando notificación Kafka: " + e.getMessage(), e);
        }
    }
}
