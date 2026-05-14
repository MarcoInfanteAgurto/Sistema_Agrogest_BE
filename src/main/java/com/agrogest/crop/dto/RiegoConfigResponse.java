package com.agrogest.crop.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RiegoConfigResponse {
    private UUID id;
    private UUID siembraId;
    private LocalDateTime ultimoRiego;
    private LocalDateTime proximoRiego;
    private Integer frecuenciaDias;
    private BigDecimal humedadSueloPct;
    private LocalDateTime updatedAt;
}
