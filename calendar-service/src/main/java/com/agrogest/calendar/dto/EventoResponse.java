package com.agrogest.calendar.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EventoResponse {
    private UUID id;
    private UUID usuarioId;
    private UUID parcelaId;
    private UUID siembraId;
    private String titulo;
    private String tipo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String color;
    private LocalDateTime createdAt;
}
