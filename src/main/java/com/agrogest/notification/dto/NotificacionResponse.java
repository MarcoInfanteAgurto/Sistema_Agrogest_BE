package com.agrogest.notification.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionResponse {
    private UUID id;
    private UUID usuarioId;
    private String tipo;
    private String titulo;
    private String mensaje;
    private Boolean leida;
    private String prioridad;
    private LocalDateTime createdAt;
    private String parcelaId;
}