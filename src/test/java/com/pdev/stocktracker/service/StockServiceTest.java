package com.pdev.stocktracker.service;

import com.pdev.stocktracker.config.JWTUserData;
import com.pdev.stocktracker.entity.Stock;
import com.pdev.stocktracker.entity.StockPurchase;
import com.pdev.stocktracker.repository.StockPurchaseRepository;
import com.pdev.stocktracker.repository.StockRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    SecurityContext securityContext;
    @Mock
    Authentication authentication;

    @InjectMocks
    StockService stockService;

    @Mock
    StockRepository stockRepository;
    @Mock
    StockPurchaseRepository stockPurchaseRepository;
    @Mock
    FindStockDetailService findStockDetailService;

    @Captor
    ArgumentCaptor<StockPurchase> stockPurchaseCaptor;

    @Captor
    ArgumentCaptor<Stock> stockCaptor;

    @Test
    @DisplayName("Deve salvar uma nova ação e sua compra com sucesso.")
    void shouldSaveANewStockWithSuccess() {
        // Arrange
        JWTUserData mockUser = JWTUserData.builder().userId("mockUserId").build();
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContextHolder.setContext(securityContext);

        Stock stock = Stock.builder()
                .stock("QQQM")
                .build();

        StockPurchase stockPurchase = StockPurchase.builder()
                .price(BigDecimal.TEN)
                .quantity(100L)
                .date(LocalDate.of(2023, 10, 1))
                .build();

        Mockito.when(stockRepository.findByStockAndUserId(stock.getStock(), mockUser.getUserId()))
                .thenReturn(Optional.empty());
        Mockito.when(stockPurchaseRepository.save(stockPurchase)).thenReturn(stockPurchase);


        // Action
        stockService.saveStock(stock, stockPurchase);

        // Assert

        Mockito.verify(stockRepository)
                .findByStockAndUserId(stock.getStock(), mockUser.getUserId());
        Mockito.verify(stockPurchaseRepository).save(stockPurchaseCaptor.capture());
        StockPurchase savedStockPurchase = stockPurchaseCaptor.getValue();
        Assertions.assertNotNull(savedStockPurchase);
        Assertions.assertNotNull(savedStockPurchase.getCreatedAt());

        Mockito.verify(stockRepository).save(stockCaptor.capture());
        Stock savedStock = stockCaptor.getValue();
        Assertions.assertNotNull(savedStock);
        Assertions.assertEquals(stock.getStock(), savedStock.getStock());
        Assertions.assertEquals(mockUser.getUserId(), savedStock.getUser().getId());
        Assertions.assertTrue(savedStock.getPurchases().contains(stockPurchase));
        Assertions.assertNotNull(savedStock.getCreatedAt());
    }

    @Test
    @DisplayName("Deve ocorrer exception ao salvar uma ação já existente.")
    void shouldNotSaveANewStock() {
        // Arrange
        JWTUserData mockUser = JWTUserData.builder().userId("mockUserId").build();
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContextHolder.setContext(securityContext);

        Stock stock = Stock.builder()
                .stock("QQQM")
                .build();

        StockPurchase stockPurchase = StockPurchase.builder()
                .price(BigDecimal.TEN)
                .quantity(100L)
                .date(LocalDate.of(2023, 10, 1))
                .build();

        Mockito.when(stockRepository.findByStockAndUserId(stock.getStock(), mockUser.getUserId()))
                .thenReturn(Optional.of(stock));

        // Action
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> {
                    stockService.saveStock(stock, stockPurchase);
                });

        // Assertions
        Assertions.assertEquals(exception.getMessage(), "Stock already exists.");
    }
}