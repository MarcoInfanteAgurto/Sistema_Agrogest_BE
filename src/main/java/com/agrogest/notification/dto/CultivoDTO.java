package com.agrogest.notification.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CultivoDTO {
    private UUID id;
    private String nombre;
    private String variedad;
    private String categoria;
    private UUID usuarioId; // Importante para saber a quién notificar
}