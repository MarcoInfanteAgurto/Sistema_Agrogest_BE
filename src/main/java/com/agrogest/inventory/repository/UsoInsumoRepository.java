package com.agrogest.inventory.repository;

import com.agrogest.inventory.model.UsoInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface UsoInsumoRepository extends JpaRepository<UsoInsumo, UUID> {

    // Todos los usos de un insumo específico
    List<UsoInsumo> findByInsumoIdOrderByFechaUsoDesc(UUID insumoId);

    // Todos los usos en una siembra
    List<UsoInsumo> findBySiembraIdOrderByFechaUsoDesc(UUID siembraId);

    // Todos los usos de un usuario
    List<UsoInsumo> findByUsuarioIdOrderByFechaUsoDesc(UUID usuarioId);
}
