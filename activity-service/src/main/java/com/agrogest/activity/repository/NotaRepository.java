package com.agrogest.activity.repository;

import com.agrogest.activity.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface NotaRepository extends JpaRepository<Nota, UUID> {
    List<Nota> findBySiembraId(UUID siembraId);
    List<Nota> findByUsuarioId(UUID usuarioId);
}
