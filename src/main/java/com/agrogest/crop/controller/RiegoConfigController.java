package com.agrogest.crop.controller;

import com.agrogest.crop.dto.*;
import com.agrogest.crop.service.RiegoConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/riego")
@RequiredArgsConstructor
public class RiegoConfigController {

    private final RiegoConfigService riegoConfigService;

    @GetMapping("/siembra/{siembraId}")
    public ResponseEntity<RiegoConfigResponse> getBySiembra(
            @PathVariable UUID siembraId) {
        return ResponseEntity.ok(riegoConfigService.getBySiembra(siembraId));
    }

    @PutMapping("/siembra/{siembraId}")
    public ResponseEntity<RiegoConfigResponse> update(
            @PathVariable UUID siembraId,
            @Valid @RequestBody UpdateRiegoConfigRequest request) {
        return ResponseEntity.ok(riegoConfigService.update(siembraId, request));
    }
}
