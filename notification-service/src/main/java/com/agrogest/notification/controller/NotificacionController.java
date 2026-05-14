package com.agrogest.notification.controller;

import com.agrogest.notification.dto.NotificacionResponse;
import com.agrogest.notification.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

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
