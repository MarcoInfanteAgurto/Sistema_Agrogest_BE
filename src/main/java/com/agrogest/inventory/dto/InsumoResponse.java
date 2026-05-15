package com.agrogest.inventory.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class InsumoResponse {
    private UUID id;
    private UUID usuarioId;
    private String nombre;
    private String categoria;
    private String unidad;
    private BigDecimal stockActual;
    private BigDecimal stockMinimo;
    private boolean stockBajo; // true si stockActual <= stockMinimo
    private OffsetDateTime createdAt;
}
