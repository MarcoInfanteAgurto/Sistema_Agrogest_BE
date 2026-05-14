package com.agrogest.activity.repository;

import com.agrogest.activity.model.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface FotoRepository extends JpaRepository<Foto, UUID> {
    List<Foto> findBySiembraId(UUID siembraId);
    List<Foto> findByUsuarioId(UUID usuarioId);
}
