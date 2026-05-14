package com.agrogest.parcel.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateParcelRequest {

    @NotNull(message = "El usuario es obligatorio")
    private UUID usuarioId;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El área es obligatoria")
    @DecimalMin(value = "0.01", message = "El área debe ser mayor a 0")
    private BigDecimal areaHectareas;

    @NotBlank(message = "El tipo de suelo es obligatorio")
    private String tipoSuelo;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    private BigDecimal latitud;

    private BigDecimal longitud;

    private String imagenMapa;
}
