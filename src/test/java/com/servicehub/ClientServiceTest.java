package com.servicehub.service;

import com.servicehub.dto.ClientPatchRequest;
import com.servicehub.dto.ClientRequest;
import com.servicehub.dto.ClientResponse;
import com.servicehub.model.Client;
import com.servicehub.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client existingClient() {
        Client client = new Client();
        client.setId(1L);
        client.setName("Maria Silva");
        client.setEmail("maria@email.com");
        client.setPhone("62999999999");
        return client;
    }

    @Test
    void findAllReturnsMappedResponses() {
        when(clientRepository.findAll()).thenReturn(List.of(existingClient()));

        List<ClientResponse> result = clientService.findAll();

        assertEquals(1, result.size());
        assertEquals("Maria Silva", result.get(0).name());
    }

    @Test
    void findByIdReturnsClientWhenItExists() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(existingClient()));

        ClientResponse result = clientService.findById(1L);

        assertEquals(1L, result.id());
        assertEquals("maria@email.com", result.email());
    }

    @Test
    void findByIdThrowsNotFoundWhenItDoesNotExist() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> clientService.findById(99L));

        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void createSanitizesTextFieldsBeforeSaving() {
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

        clientService.create(new ClientRequest("<b>Maria</b>", "maria@email.com", "62999999999"));

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(captor.capture());
        assertEquals("&lt;b&gt;Maria&lt;/b&gt;", captor.getValue().getName());
    }

    @Test
    void patchUpdatesOnlyProvidedFields() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(existingClient()));
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

        ClientResponse result = clientService.patch(1L, new ClientPatchRequest(null, null, "62988888888"));

        assertEquals("62988888888", result.phone());
        assertEquals("Maria Silva", result.name());
        assertEquals("maria@email.com", result.email());
    }

    @Test
    void deleteThrowsNotFoundAndDoesNotDeleteWhenClientDoesNotExist() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> clientService.delete(99L));

        verify(clientRepository, never()).deleteById(any());
    }
}