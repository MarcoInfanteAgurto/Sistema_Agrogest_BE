package com.agrogest.inventory.repository;

import com.agrogest.inventory.model.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface InsumoRepository extends JpaRepository<Insumo, UUID> {

    // Todos los insumos de un usuario
    List<Insumo> findByUsuarioIdOrderByNombreAsc(UUID usuarioId);

    // Insumos por categoría de un usuario
    List<Insumo> findByUsuarioIdAndCategoriaOrderByNombreAsc(UUID usuarioId, String categoria);

    // Insumos con stock bajo (stock_actual <= stock_minimo)
    List<Insumo> findByUsuarioIdAndStockActualLessThanEqual(UUID usuarioId, BigDecimal stockMinimo);
}
