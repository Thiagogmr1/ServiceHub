package com.servicehub.controller;

import com.servicehub.dto.ClientPatchRequest;
import com.servicehub.dto.ClientRequest;
import com.servicehub.dto.ClientResponse;
import com.servicehub.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<ClientResponse> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public ClientResponse findById(@PathVariable Long id) {
        return clientService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponse create(@Valid @RequestBody ClientRequest request) {
        return clientService.create(request);
    }

    @PutMapping("/{id}")
    public ClientResponse update(@PathVariable Long id, @Valid @RequestBody ClientRequest request) {
        return clientService.update(id, request);
    }

    @PatchMapping("/{id}")
    public ClientResponse patch(@PathVariable Long id, @Valid @RequestBody ClientPatchRequest request) {
        return clientService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        clientService.delete(id);
    }
}