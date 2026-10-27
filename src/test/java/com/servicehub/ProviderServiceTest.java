package com.servicehub.service;

import com.servicehub.dto.ProviderPatchRequest;
import com.servicehub.dto.ProviderRequest;
import com.servicehub.dto.ProviderResponse;
import com.servicehub.model.Provider;
import com.servicehub.repository.ProviderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private ProviderService providerService;

    private Provider existingProvider() {
        Provider provider = new Provider();
        provider.setId(1L);
        provider.setName("João Serviços");
        provider.setEmail("joao@email.com");
        provider.setPhone("62999999999");
        provider.setService("Eletricista");
        return provider;
    }

    @Test
    void findByIdThrowsNotFoundWhenItDoesNotExist() {
        when(providerRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> providerService.findById(99L));

        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void createSanitizesTextFieldsAndKeepsAccents() {
        when(providerRepository.save(any(Provider.class))).thenAnswer(inv -> inv.getArgument(0));

        providerService.create(new ProviderRequest(
                "João Serviços", "joao@email.com", "62999999999", "<script>x</script>"));

        ArgumentCaptor<Provider> captor = ArgumentCaptor.forClass(Provider.class);
        verify(providerRepository).save(captor.capture());
        assertEquals("João Serviços", captor.getValue().getName());
        assertEquals("&lt;script&gt;x&lt;/script&gt;", captor.getValue().getService());
    }

    @Test
    void patchUpdatesOnlyProvidedFields() {
        when(providerRepository.findById(1L)).thenReturn(Optional.of(existingProvider()));
        when(providerRepository.save(any(Provider.class))).thenAnswer(inv -> inv.getArgument(0));

        ProviderResponse result = providerService.patch(1L,
                new ProviderPatchRequest(null, null, null, "Encanador"));

        assertEquals("Encanador", result.service());
        assertEquals("João Serviços", result.name());
        assertEquals("joao@email.com", result.email());
    }
}