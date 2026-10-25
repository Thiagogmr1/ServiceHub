package com.servicehub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record ProviderPatchRequest(
        @Size(min = 2, max = 100, message = "name deve ter entre 2 e 100 caracteres")
        String name,

        @Email(message = "email inválido")
        String email,

        String phone,

        String service) {
}