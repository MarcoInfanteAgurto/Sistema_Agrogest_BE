package com.agrogest.notification.repository;

import com.agrogest.notification.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, UUID> {
    // Registra la búsqueda de todas las notificaciones de un usuario
    List<Notificacion> findByUsuarioId(UUID usuarioId);
    
    // Registra la búsqueda filtrando por estado de lectura (para las no leídas)
    List<Notificacion> findByUsuarioIdAndLeida(UUID usuarioId, boolean leida);
}