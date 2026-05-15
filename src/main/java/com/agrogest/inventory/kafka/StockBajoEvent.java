package com.agrogest.inventory.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockBajoEvent {
    private UUID insumoId;
    private UUID usuarioId;
    private String insumoNombre;
    private String categoria;
    private String unidad;
    private BigDecimal stockActual;
    private BigDecimal stockMinimo;
}
