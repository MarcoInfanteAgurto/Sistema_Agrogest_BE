package com.agrogest.notification.controller;

import com.agrogest.notification.dto.NotificacionResponse;
import com.agrogest.notification.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;
    // Lista de clientes conectados para tiempo real
    public static final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public SseEmitter streamNotificaciones() {
    // Definimos un timeout largo para evitar desconexiones constantes
    SseEmitter emitter = new SseEmitter(86400000L); // 24 horas
    
    emitters.add(emitter);

    emitter.onCompletion(() -> emitters.remove(emitter));
    emitter.onTimeout(() -> emitters.remove(emitter));
    emitter.onError((e) -> emitters.remove(emitter));

    return emitter;
}

    @GetMapping
    public ResponseEntity<List<NotificacionResponse>> getAll() {
        return ResponseEntity.ok(notificacionService.getAll());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacionResponse>> getByUsuario(
            @PathVariable UUID usuarioId) {
        return ResponseEntity.ok(notificacionService.getByUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/no-leidas")
    public ResponseEntity<List<NotificacionResponse>> getNoLeidas(
            @PathVariable UUID usuarioId) {
        return ResponseEntity.ok(notificacionService.getNoLeidas(usuarioId));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificacionResponse> markAsRead(
            @PathVariable UUID id) {
        return ResponseEntity.ok(notificacionService.markAsRead(id));
    }

    @PatchMapping("/usuario/{usuarioId}/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @PathVariable UUID usuarioId) {
        notificacionService.markAllAsRead(usuarioId);
        return ResponseEntity.noContent().build();
    }
}