package com.agrogest.inventory.controller;

import com.agrogest.inventory.dto.InsumoRequest;
import com.agrogest.inventory.dto.InsumoResponse;
import com.agrogest.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/insumos")
@RequiredArgsConstructor
public class InsumoController {

    private final InventoryService inventoryService;

    // POST /api/insumos
    @PostMapping
    public ResponseEntity<InsumoResponse> crear(@Valid @RequestBody InsumoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventoryService.crearInsumo(request));
    }

    // PUT /api/insumos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<InsumoResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody InsumoRequest request) {
        return ResponseEntity.ok(inventoryService.actualizarInsumo(id, request));
    }

    // DELETE /api/insumos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        inventoryService.eliminarInsumo(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/insumos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<InsumoResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(inventoryService.getInsumo(id));
    }

    // GET /api/insumos/usuario/{usuarioId}
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<InsumoResponse>> getByUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(inventoryService.getInsumosByUsuario(usuarioId));
    }

    // GET /api/insumos/usuario/{usuarioId}/categoria/{categoria}
    @GetMapping("/usuario/{usuarioId}/categoria/{categoria}")
    public ResponseEntity<List<InsumoResponse>> getByCategoria(
            @PathVariable UUID usuarioId,
            @PathVariable String categoria) {
        return ResponseEntity.ok(inventoryService.getInsumosByCategoria(usuarioId, categoria));
    }

    // GET /api/insumos/usuario/{usuarioId}/stock-bajo
    @GetMapping("/usuario/{usuarioId}/stock-bajo")
    public ResponseEntity<List<InsumoResponse>> getStockBajo(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(inventoryService.getInsumosStockBajo(usuarioId));
    }
}
