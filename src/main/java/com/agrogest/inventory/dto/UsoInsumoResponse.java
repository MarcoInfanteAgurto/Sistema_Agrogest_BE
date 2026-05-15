package com.agrogest.inventory.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class UsoInsumoResponse {
    private UUID id;
    private UUID insumoId;
    private String insumoNombre; // nombre del insumo para comodidad del frontend
    private UUID siembraId;
    private UUID usuarioId;
    private BigDecimal cantidad;
    private String unidad;
    private LocalDate fechaUso;
    private String nota;
    private OffsetDateTime createdAt;
}
