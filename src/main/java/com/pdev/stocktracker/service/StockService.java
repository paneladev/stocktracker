package com.pdev.stocktracker.service;

import com.pdev.stocktracker.client.response.BrapiStockDataResponse;
import com.pdev.stocktracker.config.SecurityContextData;
import com.pdev.stocktracker.entity.Stock;
import com.pdev.stocktracker.entity.StockPurchase;
import com.pdev.stocktracker.entity.User;
import com.pdev.stocktracker.exception.ResourceAlreadyExistsException;
import com.pdev.stocktracker.exception.ResourceNotFoundException;
import com.pdev.stocktracker.repository.StockPurchaseRepository;
import com.pdev.stocktracker.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    @Value("${stock.client.brapi.active}")
    private boolean isBrapiActive;

    private final StockRepository stockRepository;
    private final StockPurchaseRepository stockPurchaseRepository;
    private final FindStockDetailService findStockDetailService;

    @Transactional
    public Stock saveStock(Stock stock, StockPurchase stockPurchase) {
        String userId = SecurityContextData.getUserData().getUserId();

        stockRepository.findByStockAndUserId(stock.getStock(), userId)
                .ifPresent(existingStock -> { throw new ResourceAlreadyExistsException("Stock already exists."); });

        stockPurchase.setCreatedAt(LocalDateTime.now());
        StockPurchase savedStockPurchased = stockPurchaseRepository.save(stockPurchase);

        stock.setUser(User.builder().id(userId).build());
        stock.setPurchases(List.of(savedStockPurchased));
        stock.setCreatedAt(LocalDateTime.now());

        Stock savedStock = stockRepository.save(stock);
        savedStock.setPrice(stockPurchase.getPrice());

        return savedStock;
    }

    private StockPurchase savePurchase(Stock stock, StockPurchase stockPurchase) {
        stockPurchase.setCreatedAt(LocalDateTime.now());
        StockPurchase savedStockPurchased = stockPurchaseRepository.save(stockPurchase);
        stock.getPurchases().add(savedStockPurchased);
        stockRepository.save(stock);
        return savedStockPurchased;
    }

    @Transactional
    public StockPurchase addPurchase(String stockId, StockPurchase stockPurchase) {
        return stockRepository.findByIdAndUserId(stockId, SecurityContextData.getUserData().getUserId())
                .map(stock -> savePurchase(stock, stockPurchase))
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found."));
    }

    public List<Stock> findAll() {
        List<Stock> stocks = stockRepository
                .findByUser(User.builder().id(SecurityContextData.getUserData().getUserId()).build());

        if(isBrapiActive) {
            stocks.forEach(stock -> {
                Optional<BrapiStockDataResponse> brapiStockDetail = findStockDetailService.getBrapiStockDetail(stock.getStock());
                stock.setPrice(BigDecimal.valueOf(brapiStockDetail.map(BrapiStockDataResponse::getRegularMarketPrice).orElse(0.0)));
            });
        } else {
            stocks.forEach(stock -> {
                BigDecimal totalValue = stock.getPurchases().stream()
                        .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                long totalQuantity = stock.getPurchases().stream()
                        .mapToLong(StockPurchase::getQuantity)
                        .sum();
                BigDecimal avgPrice = totalQuantity > 0
                        ? totalValue.divide(BigDecimal.valueOf(totalQuantity), RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;
                stock.setPrice(avgPrice);
            });
        }

        return stocks;
    }

    public Stock findById(String id) {
        return stockRepository.findByIdAndUserId(id, SecurityContextData.getUserData().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found."));
    }

    @Transactional
    public void delete(Stock stock) {
        stock.getPurchases().forEach(stockPurchase -> stockPurchaseRepository.deleteById(stockPurchase.getId()));
        stockRepository.delete(stock);
    }

}
