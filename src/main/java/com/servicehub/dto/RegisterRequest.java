package com.servicehub.dto;

import com.servicehub.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "email é obrigatório")
        @Email(message = "email inválido")
        String email,

        @NotBlank(message = "password é obrigatório")
        @Size(min = 8, message = "password deve ter ao menos 8 caracteres")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$",
                message = "password deve conter ao menos uma letra maiúscula e um número")
        String password,

        @NotNull(message = "role é obrigatória")
        Role role) {
}