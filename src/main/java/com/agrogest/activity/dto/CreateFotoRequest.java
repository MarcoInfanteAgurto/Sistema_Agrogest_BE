package com.agrogest.activity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class CreateFotoRequest {

    @NotNull(message = "La siembra es obligatoria")
    private UUID siembraId;

    @NotNull(message = "El usuario es obligatorio")
    private UUID usuarioId;

    @NotBlank(message = "La URL es obligatoria")
    private String url;

    private String descripcion;
}
