package com.agrogest.crop.controller;

import com.agrogest.crop.dto.*;
import com.agrogest.crop.service.CultivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cultivos")
@RequiredArgsConstructor
public class CultivoController {

    private final CultivoService cultivoService;

    @PostMapping
    public ResponseEntity<CultivoResponse> create(
            @Valid @RequestBody CreateCultivoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cultivoService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CultivoResponse>> getAll() {
        return ResponseEntity.ok(cultivoService.getAll());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CultivoResponse>> getActivos() {
        return ResponseEntity.ok(cultivoService.getActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CultivoResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(cultivoService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CultivoResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody CreateCultivoRequest request) {
        return ResponseEntity.ok(cultivoService.update(id, request));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CultivoResponse> deactivate(@PathVariable UUID id) {
        return ResponseEntity.ok(cultivoService.deactivate(id));
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<CultivoResponse> restore(@PathVariable UUID id) {
        return ResponseEntity.ok(cultivoService.restore(id));
    }
}
