package com.servicehub.service;

import com.servicehub.dto.ProviderPatchRequest;
import com.servicehub.dto.ProviderRequest;
import com.servicehub.dto.ProviderResponse;
import com.servicehub.model.Provider;
import com.servicehub.repository.ProviderRepository;
import com.servicehub.util.InputSanitizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProviderService {

    private final ProviderRepository providerRepository;

    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public List<ProviderResponse> findAll() {
        return providerRepository.findAll().stream().map(ProviderResponse::from).toList();
    }

    public ProviderResponse findById(Long id) {
        return ProviderResponse.from(getOrThrow(id));
    }

    public ProviderResponse create(ProviderRequest request) {
        Provider provider = new Provider();
        provider.setName(InputSanitizer.sanitize(request.name()));
        provider.setEmail(request.email());
        provider.setPhone(InputSanitizer.sanitize(request.phone()));
        provider.setService(InputSanitizer.sanitize(request.service()));
        return ProviderResponse.from(providerRepository.save(provider));
    }

    public ProviderResponse update(Long id, ProviderRequest request) {
        Provider provider = getOrThrow(id);
        provider.setName(InputSanitizer.sanitize(request.name()));
        provider.setEmail(request.email());
        provider.setPhone(InputSanitizer.sanitize(request.phone()));
        provider.setService(InputSanitizer.sanitize(request.service()));
        return ProviderResponse.from(providerRepository.save(provider));
    }

    public ProviderResponse patch(Long id, ProviderPatchRequest request) {
        Provider provider = getOrThrow(id);
        if (request.name() != null) provider.setName(InputSanitizer.sanitize(request.name()));
        if (request.email() != null) provider.setEmail(request.email());
        if (request.phone() != null) provider.setPhone(InputSanitizer.sanitize(request.phone()));
        if (request.service() != null) provider.setService(InputSanitizer.sanitize(request.service()));
        return ProviderResponse.from(providerRepository.save(provider));
    }

    public void delete(Long id) {
        getOrThrow(id);
        providerRepository.deleteById(id);
    }

    private Provider getOrThrow(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Provider não encontrado: " + id));
    }
}