package com.agrogest.notification.service;

import com.agrogest.notification.model.Notificacion;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InMemoryNotificationService {

    private final Map<UUID, Notificacion> notificaciones = new HashMap<>();
    private final UUID defaultUserId = UUID.fromString("58b1b29a-e648-429d-b930-f19a74aaa1f8");

    public InMemoryNotificationService() {
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Sample notifications for testing
        UUID notif1Id = UUID.randomUUID();
        notificaciones.put(notif1Id, Notificacion.builder()
                .id(notif1Id)
                .usuarioId(defaultUserId)
                .tipo("Riego")
                .titulo("Riego programado")
                .mensaje("El riego de la parcela A está programado para hoy a las 14:00")
                .leida(false)
                .prioridad("Media")
                .parcelaId(UUID.randomUUID())
                .createdAt(LocalDateTime.now().minusHours(2))
                .build());

        UUID notif2Id = UUID.randomUUID();
        notificaciones.put(notif2Id, Notificacion.builder()
                .id(notif2Id)
                .usuarioId(defaultUserId)
                .tipo("Clima")
                .titulo("Alerta de clima")
                .mensaje("Se esperan lluvias fuertes en las próximas 24 horas")
                .leida(false)
                .prioridad("Alta")
                .createdAt(LocalDateTime.now().minusHours(1))
                .build());

        UUID notif3Id = UUID.randomUUID();
        notificaciones.put(notif3Id, Notificacion.builder()
                .id(notif3Id)
                .usuarioId(defaultUserId)
                .tipo("Cosecha")
                .titulo("Cosecha lista")
                .mensaje("La parcela B está lista para cosecha")
                .leida(true)
                .prioridad("Urgente")
                .parcelaId(UUID.randomUUID())
                .createdAt(LocalDateTime.now().minusDays(1))
                .build());

        UUID notif4Id = UUID.randomUUID();
        notificaciones.put(notif4Id, Notificacion.builder()
                .id(notif4Id)
                .usuarioId(defaultUserId)
                .tipo("Siembra")
                .titulo("Siembra completada")
                .mensaje("La siembra en la parcela C ha sido completada exitosamente")
                .leida(true)
                .prioridad("Baja")
                .parcelaId(UUID.randomUUID())
                .createdAt(LocalDateTime.now().minusDays(2))
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
                .sorted(Comparator.comparing(Notificacion::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public Notificacion markAsRead(UUID notificacionId) {
        // Buscar la notificación por su ID (no por la clave del mapa)
        Notificacion notif = notificaciones.values().stream()
                .filter(n -> n.getId().equals(notificacionId))
                .findFirst()
                .orElse(null);
        
        if (notif != null) {
            notif.setLeida(true);
        }
        return notif;
    }

    public void markAllAsRead(UUID usuarioId) {
        notificaciones.values().stream()
                .filter(n -> n.getUsuarioId().equals(usuarioId))
                .forEach(n -> n.setLeida(true));
    }

    public Notificacion getById(UUID id) {
        // Buscar la notificación por su ID (no por la clave del mapa)
        return notificaciones.values().stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Notificacion create(Notificacion notificacion) {
        if (notificacion.getId() == null) {
            notificacion.setId(UUID.randomUUID());
        }
        if (notificacion.getCreatedAt() == null) {
            notificacion.setCreatedAt(LocalDateTime.now());
        }
        notificaciones.put(notificacion.getId(), notificacion);
        return notificacion;
    }
}
