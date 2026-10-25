package com.servicehub.service;

import com.servicehub.dto.ClientPatchRequest;
import com.servicehub.dto.ClientRequest;
import com.servicehub.dto.ClientResponse;
import com.servicehub.model.Client;
import com.servicehub.repository.ClientRepository;
import com.servicehub.util.InputSanitizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<ClientResponse> findAll() {
        return clientRepository.findAll().stream().map(ClientResponse::from).toList();
    }

    public ClientResponse findById(Long id) {
        return ClientResponse.from(getOrThrow(id));
    }

    public ClientResponse create(ClientRequest request) {
        Client client = new Client();
        client.setName(InputSanitizer.sanitize(request.name()));
        client.setEmail(request.email());
        client.setPhone(InputSanitizer.sanitize(request.phone()));
        return ClientResponse.from(clientRepository.save(client));
    }

    public ClientResponse update(Long id, ClientRequest request) {
        Client client = getOrThrow(id);
        client.setName(InputSanitizer.sanitize(request.name()));
        client.setEmail(request.email());
        client.setPhone(InputSanitizer.sanitize(request.phone()));
        return ClientResponse.from(clientRepository.save(client));
    }

    public ClientResponse patch(Long id, ClientPatchRequest request) {
        Client client = getOrThrow(id);
        if (request.name() != null) client.setName(InputSanitizer.sanitize(request.name()));
        if (request.email() != null) client.setEmail(request.email());
        if (request.phone() != null) client.setPhone(InputSanitizer.sanitize(request.phone()));
        return ClientResponse.from(clientRepository.save(client));
    }

    public void delete(Long id) {
        getOrThrow(id);
        clientRepository.deleteById(id);
    }

    private Client getOrThrow(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client não encontrado: " + id));
    }
}