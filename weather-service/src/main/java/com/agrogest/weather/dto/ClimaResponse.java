package com.agrogest.weather.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ClimaResponse {
    private UUID id;
    private UUID parcelaId;
    private BigDecimal temperatura;
    private BigDecimal humedad;
    private BigDecimal vientoKmh;
    private BigDecimal lluviaMm;
    private String condicion;
    private BigDecimal horasSol;
    private String uvIndex;
    private LocalDateTime registradoEn;
}
