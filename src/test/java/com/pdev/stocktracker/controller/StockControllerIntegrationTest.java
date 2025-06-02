package com.pdev.stocktracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdev.stocktracker.config.JWTUserData;
import com.pdev.stocktracker.config.SecurityContextData;
import com.pdev.stocktracker.controller.request.StockRequest;
import com.pdev.stocktracker.entity.Role;
import com.pdev.stocktracker.entity.User;
import com.pdev.stocktracker.repository.StockPurchaseRepository;
import com.pdev.stocktracker.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StockControllerIntegrationTest extends BaseIntegrationTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockPurchaseRepository stockPurchaseRepository;

    private MockedStatic<SecurityContextData> securityContextDataMock;

    @BeforeEach
    void setUp() {
        // Cria os dados do usuário
        User testUser = User.builder()
                .id("user123")
                .name("Test User")
                .email("test@example.com")
                .roles(List.of(Role.USER))
                .build();

        JWTUserData userData = JWTUserData.builder()
                .userId(testUser.getId())
                .email(testUser.getEmail())
                .roles(testUser.getRoles())
                .build();


        securityContextDataMock = Mockito.mockStatic(SecurityContextData.class);
        securityContextDataMock.when(SecurityContextData::getUserData).thenReturn(userData);
    }

    @AfterEach
    void cleanup() {
        if (securityContextDataMock != null) {
            securityContextDataMock.close();
        }

        stockRepository.deleteAll();
        stockPurchaseRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldSaveStockPurchaseSuccessfully() throws Exception {
        StockRequest request = StockRequest.builder()
                .stock("AAPL")
                .quantity(10L)
                .date(LocalDate.now())
                .price(new BigDecimal("150.00"))
                .build();

        mockMvc.perform(post("/stock")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.stock").value("AAPL"));
    }

    @Test
    void shouldFailWithoutAuthentication() throws Exception {
        StockRequest request = StockRequest.builder()
                .stock("AAPL")
                .quantity(10L)
                .date(LocalDate.now())
                .price(new BigDecimal("150.00"))
                .build();

        mockMvc.perform(post("/stock")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
