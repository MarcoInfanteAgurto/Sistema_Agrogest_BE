package com.agrogest.activity.repository;

import com.agrogest.activity.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ActividadRepository extends JpaRepository<Actividad, UUID> {
    List<Actividad> findBySiembraId(UUID siembraId);
    List<Actividad> findByUsuarioId(UUID usuarioId);
    List<Actividad> findByEstado(String estado);
}
