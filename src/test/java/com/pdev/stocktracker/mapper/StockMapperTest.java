package com.pdev.stocktracker.mapper;

import com.pdev.stocktracker.controller.request.StockAddPurchaseRequest;
import com.pdev.stocktracker.controller.request.StockRequest;
import com.pdev.stocktracker.controller.response.StockPurchaseResponse;
import com.pdev.stocktracker.controller.response.StockResponse;
import com.pdev.stocktracker.entity.Stock;
import com.pdev.stocktracker.entity.StockPurchase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StockMapperTest {

    @Test
    @DisplayName("Deve converter StockRequest para Stock e StockPurchase")
    void toStock() {
        // Arrange
        StockRequest stockQQQ = StockRequest.builder()
                .stock("QQQ")
                .date(LocalDate.of(2024, 10, 1))
                .quantity(10L)
                .price(BigDecimal.TEN)
                .build();

        // Action
        Pair<Stock, StockPurchase> stock = StockMapper.toStock(stockQQQ);

        // Assertions
        assertNotNull(stock);

        Stock qqq = stock.getFirst();
        StockPurchase purchase = stock.getSecond();

        assertNotNull(qqq);
        assertNotNull(purchase);

        assertEquals("QQQ", qqq.getStock());
        assertEquals(LocalDate.of(2024, 10, 1), purchase.getDate());
        assertEquals(10L, purchase.getQuantity());
        assertEquals(BigDecimal.TEN, purchase.getPrice());
    }

    @Test
    @DisplayName("Deve converter StockAddPurchaseRequest para StockPurchase")
    void toStockPurchase() {
        // Arrange
        StockAddPurchaseRequest purchaseRequest = StockAddPurchaseRequest.builder()
                .date(LocalDate.of(2024, 10, 1))
                .quantity(10L)
                .price(BigDecimal.TEN)
                .build();

        // Action
        StockPurchase stockPurchase = StockMapper.toStockPurchase(purchaseRequest);

        // Assertions
        assertNotNull(stockPurchase);
        assertEquals(LocalDate.of(2024, 10, 1), stockPurchase.getDate());
        assertEquals(10L, stockPurchase.getQuantity());
        assertEquals(BigDecimal.TEN, stockPurchase.getPrice());
    }

    @Test
    @DisplayName("Deve converter Stock para StockResponse")
    void toStockResponse() {
        // Arrange
        Stock stock = Stock.builder()
                .id("codigo-1")
                .stock("QQQ")
                .price(BigDecimal.TEN)
                .build();

        StockPurchase purchase1 = StockPurchase.builder()
                .date(LocalDate.of(2024, 10, 1))
                .quantity(10L)
                .price(BigDecimal.TEN)
                .build();

        StockPurchase purchase2 = StockPurchase.builder()
                .date(LocalDate.of(2024, 10, 2))
                .quantity(20L)
                .price(BigDecimal.TEN)
                .build();

        stock.setPurchases(List.of(purchase1, purchase2));

        // Action
        StockResponse stockResponse = StockMapper.toStockResponse(stock);

        // Assertions
        assertNotNull(stockResponse);
        assertEquals("codigo-1", stockResponse.getId());
        assertEquals("QQQ", stockResponse.getStock());
        assertEquals(BigDecimal.TEN, stockResponse.getPrice());
        assertEquals(30L, stockResponse.getQuantity());
        assertEquals(BigDecimal.valueOf(300), stockResponse.getPriceTotal());
    }

    @Test
    @DisplayName("Deve converter StockPurchase para StockPurchaseResponse")
    void stockDetailResponse() {
        // Arrange
        StockPurchase stockPurchase = StockPurchase.builder()
                .date(LocalDate.of(2024, 10, 1))
                .quantity(10L)
                .price(BigDecimal.TEN)
                .build();

        // Action
        StockPurchaseResponse stockResponse = StockMapper.stockDetailResponse(stockPurchase, "QQQ");

        // Assertions
        assertNotNull(stockResponse);
        assertEquals(LocalDate.of(2024, 10, 1), stockResponse.getDate());
        assertEquals(10L, stockResponse.getQuantity());
        assertEquals(BigDecimal.TEN, stockResponse.getPrice());
        assertEquals(BigDecimal.valueOf(100), stockResponse.getPriceTotal());
    }
}