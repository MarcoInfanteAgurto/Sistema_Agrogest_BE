package com.agrogest.activity.controller;

import com.agrogest.activity.dto.*;
import com.agrogest.activity.service.ActividadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/actividades")
@RequiredArgsConstructor
public class ActividadController {

    private final ActividadService actividadService;

    @PostMapping
    public ResponseEntity<ActividadResponse> create(
            @Valid @RequestBody CreateActividadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(actividadService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ActividadResponse>> getAll() {
        return ResponseEntity.ok(actividadService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(actividadService.getById(id));
    }

    @GetMapping("/siembra/{siembraId}")
    public ResponseEntity<List<ActividadResponse>> getBySiembra(
            @PathVariable UUID siembraId) {
        return ResponseEntity.ok(actividadService.getBySiembra(siembraId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ActividadResponse>> getByUsuario(
            @PathVariable UUID usuarioId) {
        return ResponseEntity.ok(actividadService.getByUsuario(usuarioId));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ActividadResponse> complete(@PathVariable UUID id) {
        return ResponseEntity.ok(actividadService.complete(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ActividadResponse> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(actividadService.cancel(id));
    }
}
