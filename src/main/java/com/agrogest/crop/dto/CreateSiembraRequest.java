package com.agrogest.crop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateSiembraRequest {

    @NotNull(message = "La parcela es obligatoria")
    private UUID parcelaId;

    @NotNull(message = "El cultivo es obligatorio")
    private UUID cultivoId;

    @NotNull(message = "El usuario es obligatorio")
    private UUID usuarioId;

    @NotNull(message = "La fecha de siembra es obligatoria")
    private LocalDate fechaSiembra;

    private LocalDate fechaCosechaEstimada;
    private BigDecimal areaSembradaHa;
    private Integer densidadPlantasHa;
}
