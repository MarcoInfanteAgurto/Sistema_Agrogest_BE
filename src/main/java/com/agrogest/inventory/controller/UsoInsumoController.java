package com.agrogest.inventory.controller;

import com.agrogest.inventory.dto.UsoInsumoRequest;
import com.agrogest.inventory.dto.UsoInsumoResponse;
import com.agrogest.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/uso-insumos")
@RequiredArgsConstructor
public class UsoInsumoController {

    private final InventoryService inventoryService;

    // POST /api/uso-insumos
    // Registra el uso de un insumo en una siembra y descuenta el stock automáticamente
    @PostMapping
    public ResponseEntity<UsoInsumoResponse> registrarUso(@Valid @RequestBody UsoInsumoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventoryService.registrarUso(request));
    }

    // GET /api/uso-insumos/insumo/{insumoId}
    @GetMapping("/insumo/{insumoId}")
    public ResponseEntity<List<UsoInsumoResponse>> getByInsumo(@PathVariable UUID insumoId) {
        return ResponseEntity.ok(inventoryService.getUsosByInsumo(insumoId));
    }

    // GET /api/uso-insumos/siembra/{siembraId}
    @GetMapping("/siembra/{siembraId}")
    public ResponseEntity<List<UsoInsumoResponse>> getBySiembra(@PathVariable UUID siembraId) {
        return ResponseEntity.ok(inventoryService.getUsosBySiembra(siembraId));
    }

    // GET /api/uso-insumos/usuario/{usuarioId}
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<UsoInsumoResponse>> getByUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(inventoryService.getUsosByUsuario(usuarioId));
    }
}
