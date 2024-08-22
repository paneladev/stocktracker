package com.pdev.stocktracker.service;

import com.pdev.stocktracker.client.BrapiClient;
import com.pdev.stocktracker.client.response.BrapiStockDataResponse;
import com.pdev.stocktracker.client.response.BrapiStockResponse;
import com.pdev.stocktracker.entity.Stock;
import com.pdev.stocktracker.entity.StockPurchase;
import com.pdev.stocktracker.repository.StockPurchaseRepository;
import com.pdev.stocktracker.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockPurchaseRepository stockPurchaseRepository;
    private final FindStockDetailService findStockDetailService;

    public Stock savePurchase(Stock stock, StockPurchase stockPurchase) {
        StockPurchase savedStockPurchased = stockPurchaseRepository.save(stockPurchase);
        stock.setPurchases(List.of(savedStockPurchased));
        return stockRepository.save(stock);
    }

    public Stock addPurchase(String stockId, StockPurchase stockPurchase) {
        Optional<Stock> optStock = stockRepository.findById(stockId);

        return optStock.map(stock -> {
            StockPurchase savedStockPurchased = stockPurchaseRepository.save(stockPurchase);
            stock.getPurchases().add(savedStockPurchased);
            return stockRepository.save(stock);
        }).orElseThrow(() -> new IllegalArgumentException("Cannot find a stock."));
    }

    public List<Stock> findAll() {
        List<Stock> stocks = stockRepository.findAll();

        stocks.forEach(stock -> {
            Optional<BrapiStockDataResponse> brapiStockDetail = findStockDetailService.getBrapiStockDetail(stock.getStock());
            stock.setPrice(BigDecimal.valueOf(brapiStockDetail.map(BrapiStockDataResponse::getRegularMarketPrice).orElse(0.0)));
        });

        return stocks;
    }

}
