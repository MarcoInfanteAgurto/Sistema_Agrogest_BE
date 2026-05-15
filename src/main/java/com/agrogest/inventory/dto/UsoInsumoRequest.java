package com.agrogest.inventory.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class UsoInsumoRequest {

    @NotNull(message = "El insumo_id es obligatorio")
    private UUID insumoId;

    @NotNull(message = "El siembra_id es obligatorio")
    private UUID siembraId;

    @NotNull(message = "El usuario_id es obligatorio")
    private UUID usuarioId;

    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.01", message = "La cantidad debe ser mayor a 0")
    private BigDecimal cantidad;

    @NotNull(message = "La fecha_uso es obligatoria")
    private LocalDate fechaUso;

    private String nota;
}
