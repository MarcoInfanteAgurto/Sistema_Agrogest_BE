package com.agrogest.activity.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FotoResponse {
    private UUID id;
    private UUID siembraId;
    private UUID usuarioId;
    private String url;
    private String descripcion;
    private LocalDateTime createdAt;
}
