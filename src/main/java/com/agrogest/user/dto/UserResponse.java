package com.agrogest.user.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String rol;
    private String fotoPerfil;
    private Boolean activo;
    private LocalDateTime createdAt;
}
