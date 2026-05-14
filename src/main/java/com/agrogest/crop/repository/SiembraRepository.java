package com.agrogest.crop.repository;

import com.agrogest.crop.model.Siembra;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SiembraRepository extends JpaRepository<Siembra, UUID> {
    List<Siembra> findByUsuarioId(UUID usuarioId);
    List<Siembra> findByParcelaId(UUID parcelaId);
    List<Siembra> findByEstado(String estado);
}
