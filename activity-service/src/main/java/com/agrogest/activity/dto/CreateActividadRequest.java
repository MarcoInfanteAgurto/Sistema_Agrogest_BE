package com.agrogest.activity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateActividadRequest {

    @NotNull(message = "La siembra es obligatoria")
    private UUID siembraId;

    @NotNull(message = "El usuario es obligatorio")
    private UUID usuarioId;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    private String descripcion;
    private LocalDate fecha;
    private BigDecimal costo;
    private String estado;
}
