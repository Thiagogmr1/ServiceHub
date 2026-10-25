package com.servicehub.dto;

import com.servicehub.model.Provider;

public record ProviderResponse(Long id, String name, String email, String phone, String service) {

    public static ProviderResponse from(Provider provider) {
        return new ProviderResponse(provider.getId(), provider.getName(), provider.getEmail(),
                provider.getPhone(), provider.getService());
    }
}