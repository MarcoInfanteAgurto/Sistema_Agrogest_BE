package com.agrogest.inventory.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class InsumoRequest {

    @NotNull(message = "El usuario_id es obligatorio")
    private UUID usuarioId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150)
    private String nombre;

    @Size(max = 100)
    private String categoria; // 'Fertilizante', 'Pesticida', 'Semilla', 'Herramienta'

    @Size(max = 50)
    private String unidad; // 'kg', 'litros', 'unidades'

    @NotNull(message = "El stock_actual es obligatorio")
    @DecimalMin(value = "0.0", message = "El stock no puede ser negativo")
    private BigDecimal stockActual;

    @DecimalMin(value = "0.0", message = "El stock mínimo no puede ser negativo")
    private BigDecimal stockMinimo;
}
