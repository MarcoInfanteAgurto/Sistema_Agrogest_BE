package com.agrogest.crop.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SiembraResponse {
    private UUID id;
    private UUID parcelaId;
    private UUID cultivoId;
    private UUID usuarioId;
    private LocalDate fechaSiembra;
    private LocalDate fechaCosechaEstimada;
    private LocalDate fechaCosechaReal;
    private BigDecimal progresoPorcentaje;
    private String etapaActual;
    private BigDecimal areaSembradaHa;
    private Integer densidadPlantasHa;
    private String estado;
    private LocalDateTime createdAt;
}
