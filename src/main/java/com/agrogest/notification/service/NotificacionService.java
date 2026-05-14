package com.agrogest.notification.service;

import com.agrogest.notification.dto.NotificacionResponse;
import java.util.List;
import java.util.UUID;

public interface NotificacionService {
    List<NotificacionResponse> getByUsuario(UUID usuarioId);
    List<NotificacionResponse> getNoLeidas(UUID usuarioId);
    NotificacionResponse markAsRead(UUID id);
    void markAllAsRead(UUID usuarioId);
}
