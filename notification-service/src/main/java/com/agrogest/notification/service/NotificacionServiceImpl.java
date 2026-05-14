package com.agrogest.notification.service;

import com.agrogest.notification.dto.NotificacionResponse;
import com.agrogest.notification.model.Notificacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final InMemoryNotificationService inMemoryService;

    @Override
    public List<NotificacionResponse> getByUsuario(UUID usuarioId) {
        return inMemoryService.getByUsuario(usuarioId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<NotificacionResponse> getNoLeidas(UUID usuarioId) {
        return inMemoryService.getNoLeidas(usuarioId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public NotificacionResponse markAsRead(UUID id) {
        Notificacion notif = inMemoryService.markAsRead(id);
        if (notif == null) {
            throw new RuntimeException("Notificación no encontrada: " + id);
        }
        return toResponse(notif);
    }

    @Override
    public void markAllAsRead(UUID usuarioId) {
        inMemoryService.markAllAsRead(usuarioId);
    }

    private NotificacionResponse toResponse(Notificacion n) {
        NotificacionResponse res = new NotificacionResponse();
        res.setId(n.getId());
        res.setUsuarioId(n.getUsuarioId());
        res.setTipo(n.getTipo());
        res.setTitulo(n.getTitulo());
        res.setMensaje(n.getMensaje());
        res.setLeida(n.getLeida());
        res.setPrioridad(n.getPrioridad());
        res.setParcelaId(n.getParcelaId());
        res.setCreatedAt(n.getCreatedAt());
        return res;
    }
}
