package com.agrogest.activity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class CreateNotaRequest {

    @NotNull(message = "La siembra es obligatoria")
    private UUID siembraId;

    @NotNull(message = "El usuario es obligatorio")
    private UUID usuarioId;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "El contenido es obligatorio")
    private String contenido;
}
