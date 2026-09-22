package com.servicehub.service;

import com.servicehub.model.Client;
import com.servicehub.repository.ClientRepository;
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

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client não encontrado: " + id));
    }

    public Client create(Client client) {
        return clientRepository.save(client);
    }

    public Client update(Long id, Client client) {
        findById(id);
        client.setId(id);
        return clientRepository.save(client);
    }

    public Client patch(Long id, Client partial) {
        Client existing = findById(id);
        if (partial.getName() != null) existing.setName(partial.getName());
        if (partial.getEmail() != null) existing.setEmail(partial.getEmail());
        if (partial.getPhone() != null) existing.setPhone(partial.getPhone());
        return clientRepository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        clientRepository.deleteById(id);
    }
}