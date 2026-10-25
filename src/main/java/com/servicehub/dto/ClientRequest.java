package com.servicehub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientRequest(
        @NotBlank(message = "name é obrigatório")
        @Size(min = 2, max = 100, message = "name deve ter entre 2 e 100 caracteres")
        String name,

        @NotBlank(message = "email é obrigatório")
        @Email(message = "email inválido")
        String email,

        @NotBlank(message = "phone é obrigatório")
        String phone) {
}