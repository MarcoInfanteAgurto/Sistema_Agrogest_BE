package com.agrogest.notification.service;

import com.agrogest.notification.model.Notificacion;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// Hemos quitado @Service para que Spring use la implementación de Base de Datos
public class InMemoryNotificationService {

    private final Map<UUID, Notificacion> notificaciones = new HashMap<>();
    private final UUID defaultUserId = UUID.fromString("58b1b29a-e648-429d-b930-f19a74aaa1f8");

    public InMemoryNotificationService() {
        initializeSampleData();
    }

    private void initializeSampleData() {
        UUID notif1Id = UUID.randomUUID();
        notificaciones.put(notif1Id, Notificacion.builder()
                .id(notif1Id)
                .usuarioId(defaultUserId)
                .tipo("Riego")
                .titulo("Riego programado")
                .mensaje("El riego de la parcela A está programado para hoy")
                .leida(false)
                .prioridad("Media")
                .createdAt(LocalDateTime.now().minusHours(2))
                .build());
    }

    public List<Notificacion> getByUsuario(UUID usuarioId) {
        return notificaciones.values().stream()
                .filter(n -> n.getUsuarioId().equals(usuarioId))
                .sorted(Comparator.comparing(Notificacion::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public List<Notificacion> getNoLeidas(UUID usuarioId) {
        return notificaciones.values().stream()
                .filter(n -> n.getUsuarioId().equals(usuarioId) && !n.getLeida())
                .collect(Collectors.toList());
    }

    public Notificacion markAsRead(UUID notificacionId) {
        Notificacion notif = notificaciones.get(notificacionId);
        if (notif != null) notif.setLeida(true);
        return notif;
    }

    public void markAllAsRead(UUID usuarioId) {
        notificaciones.values().stream()
                .filter(n -> n.getUsuarioId().equals(usuarioId))
                .forEach(n -> n.setLeida(true));
    }

    public Notificacion create(Notificacion notificacion) {
        if (notificacion.getId() == null) notificacion.setId(UUID.randomUUID());
        notificaciones.put(notificacion.getId(), notificacion);
        return notificacion;
    }
}