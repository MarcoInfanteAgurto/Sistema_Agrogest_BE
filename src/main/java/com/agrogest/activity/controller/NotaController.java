package com.agrogest.activity.controller;

import com.agrogest.activity.dto.*;
import com.agrogest.activity.service.NotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notas")
@RequiredArgsConstructor
public class NotaController {

    private final NotaService notaService;

    @PostMapping
    public ResponseEntity<NotaResponse> create(
            @Valid @RequestBody CreateNotaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notaService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(notaService.getById(id));
    }

    @GetMapping("/siembra/{siembraId}")
    public ResponseEntity<List<NotaResponse>> getBySiembra(
            @PathVariable UUID siembraId) {
        return ResponseEntity.ok(notaService.getBySiembra(siembraId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotaResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody CreateNotaRequest request) {
        return ResponseEntity.ok(notaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        notaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
