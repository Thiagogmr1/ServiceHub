package com.servicehub.controller;

import com.servicehub.dto.ProviderPatchRequest;
import com.servicehub.dto.ProviderRequest;
import com.servicehub.dto.ProviderResponse;
import com.servicehub.service.ProviderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping
    public List<ProviderResponse> findAll() {
        return providerService.findAll();
    }

    @GetMapping("/{id}")
    public ProviderResponse findById(@PathVariable Long id) {
        return providerService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','PROVIDER')")
    public ProviderResponse create(@Valid @RequestBody ProviderRequest request) {
        return providerService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROVIDER')")
    public ProviderResponse update(@PathVariable Long id, @Valid @RequestBody ProviderRequest request) {
        return providerService.update(id, request);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROVIDER')")
    public ProviderResponse patch(@PathVariable Long id, @Valid @RequestBody ProviderPatchRequest request) {
        return providerService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        providerService.delete(id);
    }
}