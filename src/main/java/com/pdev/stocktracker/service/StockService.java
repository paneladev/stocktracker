package com.pdev.stocktracker.service;

import com.pdev.stocktracker.client.response.BrapiStockDataResponse;
import com.pdev.stocktracker.config.SecurityContextData;
import com.pdev.stocktracker.entity.Stock;
import com.pdev.stocktracker.entity.StockPurchase;
import com.pdev.stocktracker.entity.User;
import com.pdev.stocktracker.repository.StockPurchaseRepository;
import com.pdev.stocktracker.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockPurchaseRepository stockPurchaseRepository;
    private final FindStockDetailService findStockDetailService;

    @Transactional
    public Stock saveStock(Stock stock, StockPurchase stockPurchase) {
        String userId = SecurityContextData.getUserData().getUserId();

        Optional<Stock> optStock = stockRepository.findByStockAndUserId(stock.getStock(), userId);
        if (optStock.isPresent()) {
            Stock savedStock = optStock.get();
            return this.savePurchase(savedStock, stockPurchase);
        }

        StockPurchase savedStockPurchased = stockPurchaseRepository.save(stockPurchase);
        stock.setUser(User.builder().id(userId).build());
        stock.setPurchases(List.of(savedStockPurchased));
        return stockRepository.save(stock);
    }

    private Stock savePurchase(Stock stock, StockPurchase stockPurchase) {
        StockPurchase savedStockPurchased = stockPurchaseRepository.save(stockPurchase);
        stock.getPurchases().add(savedStockPurchased);
        return stockRepository.save(stock);
    }

    @Transactional
    public Stock addPurchase(String stockId, StockPurchase stockPurchase) {
        return stockRepository.findByIdAndUserId(stockId, SecurityContextData.getUserData().getUserId())
                .map(stock -> savePurchase(stock, stockPurchase))
                .orElseThrow(() -> new IllegalArgumentException("Cannot find a stock."));
    }

    public List<Stock> findAll() {
        List<Stock> stocks = stockRepository
                .findByUser(User.builder().id(SecurityContextData.getUserData().getUserId()).build());

        stocks.forEach(stock -> {
            Optional<BrapiStockDataResponse> brapiStockDetail = findStockDetailService.getBrapiStockDetail(stock.getStock());
            stock.setPrice(BigDecimal.valueOf(brapiStockDetail.map(BrapiStockDataResponse::getRegularMarketPrice).orElse(0.0)));
        });

        return stocks;
    }

    public Optional<Stock> findById(String id) {
        return stockRepository.findByIdAndUserId(id, SecurityContextData.getUserData().getUserId());
    }

    @Transactional
    public void delete(Stock stock) {
        stock.getPurchases().forEach(stockPurchase -> stockPurchaseRepository.deleteById(stockPurchase.getId()));
        stockRepository.delete(stock);
    }

}
