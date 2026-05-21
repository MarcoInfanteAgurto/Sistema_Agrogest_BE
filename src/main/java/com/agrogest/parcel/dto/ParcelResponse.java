package com.agrogest.parcel.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ParcelResponse {
    private UUID id;
    private UUID usuarioId;
    private String nombre;
    private BigDecimal areaHectareas;
    private String tipoSuelo;
    private String estado;
    private UUID cultivoActualId;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private String imagenMapa;
    private LocalDateTime createdAt;
    private Boolean activo;
}
