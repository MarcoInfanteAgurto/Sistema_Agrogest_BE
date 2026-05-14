package com.agrogest.crop.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CultivoResponse {
    private UUID id;
    private String nombre;
    private String variedad;
    private String imagen;
    private Integer diasCrecimientoMin;
    private Integer diasCrecimientoMax;
    private String nivelRiego;
    private String tipoSueloIdeal;
    private String temporada;
    private String categoria;
    private Boolean activo;
    private LocalDateTime createdAt;
}
