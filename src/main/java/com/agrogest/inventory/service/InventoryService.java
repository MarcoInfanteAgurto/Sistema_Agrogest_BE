package com.agrogest.inventory.service;

import com.agrogest.inventory.dto.*;
import java.util.List;
import java.util.UUID;

public interface InventoryService {

    // ── INSUMOS ──────────────────────────────────────────────
    InsumoResponse crearInsumo(InsumoRequest request);
    InsumoResponse actualizarInsumo(UUID id, InsumoRequest request);
    void eliminarInsumo(UUID id);
    InsumoResponse getInsumo(UUID id);
    List<InsumoResponse> getInsumosByUsuario(UUID usuarioId);
    List<InsumoResponse> getInsumosByCategoria(UUID usuarioId, String categoria);
    List<InsumoResponse> getInsumosStockBajo(UUID usuarioId);

    // ── USO DE INSUMOS ───────────────────────────────────────
    UsoInsumoResponse registrarUso(UsoInsumoRequest request);
    List<UsoInsumoResponse> getUsosByInsumo(UUID insumoId);
    List<UsoInsumoResponse> getUsosBySiembra(UUID siembraId);
    List<UsoInsumoResponse> getUsosByUsuario(UUID usuarioId);
}
