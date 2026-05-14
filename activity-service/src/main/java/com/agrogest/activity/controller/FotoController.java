package com.agrogest.activity.controller;

import com.agrogest.activity.dto.*;
import com.agrogest.activity.service.FotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/fotos")
@RequiredArgsConstructor
public class FotoController {

    private final FotoService fotoService;

    @PostMapping
    public ResponseEntity<FotoResponse> create(
            @Valid @RequestBody CreateFotoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fotoService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FotoResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(fotoService.getById(id));
    }

    @GetMapping("/siembra/{siembraId}")
    public ResponseEntity<List<FotoResponse>> getBySiembra(
            @PathVariable UUID siembraId) {
        return ResponseEntity.ok(fotoService.getBySiembra(siembraId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        fotoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
