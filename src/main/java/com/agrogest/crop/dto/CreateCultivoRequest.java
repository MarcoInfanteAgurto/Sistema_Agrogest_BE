package com.agrogest.crop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCultivoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String variedad;
    private String imagen;
    private Integer diasCrecimientoMin;
    private Integer diasCrecimientoMax;
    private String nivelRiego;
    private String tipoSueloIdeal;
    private String temporada;
    private String categoria;
}
