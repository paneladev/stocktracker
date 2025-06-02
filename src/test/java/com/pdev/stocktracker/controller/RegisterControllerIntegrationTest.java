package com.pdev.stocktracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdev.stocktracker.controller.request.RegisterUserRequest;
import com.pdev.stocktracker.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class RegisterControllerIntegrationTest extends BaseIntegrationTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUserWithRoleUser() throws Exception {
        // Arrange - Given
        RegisterUserRequest request = createRegisterUserRequest();

        // Action/When - Assertions/Then
        mockMvc.perform(post("/stock/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        // Arrange - Given
        RegisterUserRequest request = createRegisterUserRequest();

        // Action/When - Assertions/Then
        mockMvc.perform(post("/stock/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Action/When - Assertions/Then
        mockMvc.perform(post("/stock/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

    }

    private RegisterUserRequest createRegisterUserRequest() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setNome("Test User");
        request.setEmail("test@example.com");
        request.setSenha("password123");
        return request;
    }
}
