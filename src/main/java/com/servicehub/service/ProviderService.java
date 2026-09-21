package com.servicehub.service;

import com.servicehub.model.Provider;
import com.servicehub.repository.ProviderRepository;
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

    public List<Provider> findAll() {
        return providerRepository.findAll();
    }

    public Provider findById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Provider não encontrado: " + id));
    }

    public Provider create(Provider provider) {
        return providerRepository.save(provider);
    }

    public Provider update(Long id, Provider provider) {
        findById(id); // lança 404 se não existir
        provider.setId(id);
        return providerRepository.save(provider);
    }

    public void delete(Long id) {
        findById(id); // lança 404 se não existir
        providerRepository.deleteById(id);
    }
}