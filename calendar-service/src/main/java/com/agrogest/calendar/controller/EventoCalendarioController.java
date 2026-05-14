package com.agrogest.calendar.controller;

import com.agrogest.calendar.dto.*;
import com.agrogest.calendar.service.EventoCalendarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequiredArgsConstructor
public class EventoCalendarioController {

    private final EventoCalendarioService eventoService;

    // POST /api/eventos
    @PostMapping
    public ResponseEntity<EventoResponse> create(
            @Valid @RequestBody CreateEventoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventoService.create(request));
    }

    // GET /api/eventos - Obtener todos los eventos
    @GetMapping
    public ResponseEntity<List<EventoResponse>> getAll() {
        return ResponseEntity.ok(eventoService.getAll());
    }

    // GET /api/eventos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<EventoResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventoService.getById(id));
    }

    // GET /api/eventos/usuario/{usuarioId}
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EventoResponse>> getByUsuario(
            @PathVariable UUID usuarioId) {
        return ResponseEntity.ok(eventoService.getByUsuario(usuarioId));
    }

    // GET /api/eventos/usuario/{usuarioId}/rango?desde=2025-06-01&hasta=2025-06-30
    @GetMapping("/usuario/{usuarioId}/rango")
    public ResponseEntity<List<EventoResponse>> getByRango(
            @PathVariable UUID usuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(
                eventoService.getByUsuarioAndFecha(usuarioId, desde, hasta));
    }

    // PUT /api/eventos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<EventoResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateEventoRequest request) {
        return ResponseEntity.ok(eventoService.update(id, request));
    }

    // DELETE /api/eventos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        eventoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
