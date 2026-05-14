package com.agrogest.activity.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ActividadResponse {
    private UUID id;
    private UUID siembraId;
    private UUID usuarioId;
    private String tipo;
    private String titulo;
    private String descripcion;
    private LocalDate fecha;
    private BigDecimal costo;
    private String estado;
    private LocalDateTime createdAt;
}
