package com.agrogest.parcel.controller;

import com.agrogest.parcel.dto.*;
import com.agrogest.parcel.service.ParcelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parcels")
@RequiredArgsConstructor
public class ParcelController {

    private final ParcelService parcelService;

    @PostMapping
    public ResponseEntity<ParcelResponse> create(
            @Valid @RequestBody CreateParcelRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(parcelService.createParcel(request));
    }

    @GetMapping
    public ResponseEntity<List<ParcelResponse>> getAll() {
        return ResponseEntity.ok(parcelService.getAllParcels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParcelResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(parcelService.getParcel(id));
    }

    @GetMapping("/user/{usuarioId}")
    public ResponseEntity<List<ParcelResponse>> getByUser(
            @PathVariable UUID usuarioId) {
        return ResponseEntity.ok(parcelService.getParcelsByUser(usuarioId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParcelResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateParcelRequest request) {
        return ResponseEntity.ok(parcelService.updateParcel(id, request));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ParcelResponse> deactivate(@PathVariable UUID id) {
        return ResponseEntity.ok(parcelService.deactivateParcel(id));
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ParcelResponse> restore(@PathVariable UUID id) {
        return ResponseEntity.ok(parcelService.restoreParcel(id));
    }
}
