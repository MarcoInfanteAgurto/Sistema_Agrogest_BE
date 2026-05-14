package com.agrogest.user.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Email(message = "Email inválido")
    @NotBlank
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    private String telefono;

    @NotBlank
    private String rol;
}
