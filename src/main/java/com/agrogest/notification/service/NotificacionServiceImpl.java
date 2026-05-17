package com.agrogest.notification.service;

import com.agrogest.notification.dto.NotificacionResponse;
import com.agrogest.notification.model.Notificacion;
import com.agrogest.notification.repository.NotificacionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository repository; 
    private final ObjectMapper objectMapper; 

    @Override
    public List<NotificacionResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public NotificacionResponse procesarYGuardar(String message) {
        try {
            Notificacion nuevaNotif = objectMapper.readValue(message, Notificacion.class);
            
            if (nuevaNotif.getCreatedAt() == null) nuevaNotif.setCreatedAt(LocalDateTime.now());
            if (nuevaNotif.getLeida() == null) nuevaNotif.setLeida(false);

            Notificacion guardada = repository.save(nuevaNotif);
            return toResponse(guardada);
        } catch (Exception e) {
            throw new RuntimeException("Error procesando Kafka: " + e.getMessage());
        }
    }

    @Override
    public List<NotificacionResponse> getByUsuario(UUID usuarioId) {
        return repository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<NotificacionResponse> getNoLeidas(UUID usuarioId) {
        return repository.findByUsuarioIdAndLeida(usuarioId, false)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public NotificacionResponse markAsRead(UUID id) {
        Notificacion notif = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No encontrada: " + id));
        notif.setLeida(true);
        return toResponse(repository.save(notif));
    }

    @Override
    public void markAllAsRead(UUID usuarioId) {
        List<Notificacion> noLeidas = repository.findByUsuarioIdAndLeida(usuarioId, false);
        noLeidas.forEach(n -> n.setLeida(true));
        repository.saveAll(noLeidas);
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
        
        res.setParcelaId(n.getParcelaId() != null ? n.getParcelaId().toString() : null);
        res.setCreatedAt(n.getCreatedAt());
        return res;
    }
}