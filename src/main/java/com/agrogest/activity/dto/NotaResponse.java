package com.agrogest.activity.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotaResponse {
    private UUID id;
    private UUID siembraId;
    private UUID usuarioId;
    private String titulo;
    private String contenido;
    private LocalDateTime createdAt;
}
