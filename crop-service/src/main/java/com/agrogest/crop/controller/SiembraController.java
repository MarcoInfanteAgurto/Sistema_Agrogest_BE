package com.agrogest.crop.controller;

import com.agrogest.crop.dto.*;
import com.agrogest.crop.service.SiembraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/siembras")
@RequiredArgsConstructor
public class SiembraController {

    private final SiembraService siembraService;

    @PostMapping
    public ResponseEntity<SiembraResponse> create(
            @Valid @RequestBody CreateSiembraRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(siembraService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<SiembraResponse>> getAll() {
        return ResponseEntity.ok(siembraService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SiembraResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(siembraService.getById(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<SiembraResponse>> getByUsuario(
            @PathVariable UUID usuarioId) {
        return ResponseEntity.ok(siembraService.getByUsuario(usuarioId));
    }

    @GetMapping("/parcela/{parcelaId}")
    public ResponseEntity<List<SiembraResponse>> getByParcela(
            @PathVariable UUID parcelaId) {
        return ResponseEntity.ok(siembraService.getByParcela(parcelaId));
    }

    @PatchMapping("/{id}/finalize")
    public ResponseEntity<SiembraResponse> finalize(@PathVariable UUID id) {
        return ResponseEntity.ok(siembraService.finalize(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<SiembraResponse> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(siembraService.cancel(id));
    }
}
