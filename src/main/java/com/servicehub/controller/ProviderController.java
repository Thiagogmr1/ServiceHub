package com.servicehub.controller;

import com.servicehub.model.Provider;
import com.servicehub.repository.ProviderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    private final ProviderRepository providerRepository;

    public ProviderController(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @GetMapping
    public List<Provider> findAll() {
        return providerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Provider findById(@PathVariable Long id) {
        return providerRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Provider create(@RequestBody Provider provider) {
        return providerRepository.save(provider);
    }

    @PutMapping("/{id}")
    public Provider update(@PathVariable Long id, @RequestBody Provider provider) {
        provider.setId(id);
        return providerRepository.save(provider);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        providerRepository.deleteById(id);
    }
}