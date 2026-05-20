package com.agrogest.notification.repository;

import com.agrogest.notification.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, UUID> {
    List<Notificacion> findByUsuarioId(UUID usuarioId);
    
    List<Notificacion> findByUsuarioIdAndLeida(UUID usuarioId, boolean leida);
}