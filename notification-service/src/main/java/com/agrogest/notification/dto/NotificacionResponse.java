package com.agrogest.notification.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificacionResponse {
    private UUID id;
    private UUID usuarioId;
    private String tipo;
    private String titulo;
    private String mensaje;
    private Boolean leida;
    private String prioridad;
    private UUID parcelaId;
    private LocalDateTime createdAt;
}
