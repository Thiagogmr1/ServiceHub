package com.servicehub;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ServiceHubIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String login(String email, String password) throws Exception {
        String body = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.token");
    }

    @Test
    void requestWithoutTokenReturns401() throws Exception {
        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginWithWrongPasswordReturns401() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@servicehub.com\",\"password\":\"errada\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminCanRunTheFullClientCrud() throws Exception {
        String auth = "Bearer " + login("admin@servicehub.com", "Admin123");

        String created = mockMvc.perform(post("/api/clients")
                        .header("Authorization", auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Maria Silva\",\"email\":\"maria@email.com\",\"phone\":\"62999999999\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(created, "$.id");

        mockMvc.perform(get("/api/clients/" + id).header("Authorization", auth))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/api/clients/" + id)
                        .header("Authorization", auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"62988888888\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("62988888888"));

        mockMvc.perform(delete("/api/clients/" + id).header("Authorization", auth))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/clients/" + id).header("Authorization", auth))
                .andExpect(status().isNotFound());
    }

    @Test
    void clientRoleCannotListClients() throws Exception {
        String email = "cliente-" + UUID.randomUUID() + "@email.com";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"Senha123\",\"role\":\"CLIENT\"}"))
                .andExpect(status().isCreated());

        String auth = "Bearer " + login(email, "Senha123");

        mockMvc.perform(get("/api/clients").header("Authorization", auth))
                .andExpect(status().isForbidden());
    }
}