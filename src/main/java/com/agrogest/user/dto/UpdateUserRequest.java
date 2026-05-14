package com.agrogest.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Email(message = "Email inválido")
    @NotBlank
    private String email;

    private String telefono;

    private String fotoPerfil;

    @NotBlank
    private String rol;
}
