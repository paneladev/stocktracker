package com.pdev.stocktracker.controller;

import com.pdev.stocktracker.controller.request.StockAddPurchaseRequest;
import com.pdev.stocktracker.controller.request.StockRequest;
import com.pdev.stocktracker.controller.response.StockPurchaseResponse;
import com.pdev.stocktracker.controller.response.StockResponse;
import com.pdev.stocktracker.entity.Stock;
import com.pdev.stocktracker.entity.StockPurchase;
import com.pdev.stocktracker.mapper.StockMapper;
import com.pdev.stocktracker.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StockResponse> savePurchase(@RequestBody StockRequest request) {
        Pair<Stock, StockPurchase> stock = StockMapper.toStock(request);
        Stock savedStock = stockService.savePurchase(stock.getFirst(), stock.getSecond());
        return ResponseEntity.status(HttpStatus.CREATED).body(StockMapper.toStockResponse(savedStock));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Stock> addPurchase(@RequestBody StockAddPurchaseRequest request) {
        try {
            Stock stock = stockService.addPurchase(request.getStockId(), StockMapper.toStockPurchase(request));
            return ResponseEntity.ok(stock);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<StockResponse>> getStocks() {
        final List<Stock> stocks = stockService.findAll();
        List<StockResponse> response = stocks.stream()
                .map(StockMapper::toStockResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{stockId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<StockPurchaseResponse>> getPurchasesByStockId(@PathVariable String stockId) {
        return stockService.findById(stockId)
                .map(stock -> {
                    final List<StockPurchaseResponse> stockPurchaseResponseList = stock.getPurchases()
                            .stream()
                            .map(StockMapper::stockDetailResponse)
                            .toList();

                    return ResponseEntity.ok(stockPurchaseResponseList);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{stockId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteStock(@PathVariable String stockId) {
        final Optional<Stock> optStock = stockService.findById(stockId);

        if (optStock.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        stockService.delete(optStock.get());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
