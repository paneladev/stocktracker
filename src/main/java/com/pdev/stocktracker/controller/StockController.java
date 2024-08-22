package com.pdev.stocktracker.controller;

import com.pdev.stocktracker.controller.request.StockAddPurchaseRequest;
import com.pdev.stocktracker.controller.request.StockRequest;
import com.pdev.stocktracker.entity.Stock;
import com.pdev.stocktracker.entity.StockPurchase;
import com.pdev.stocktracker.mapper.StockMapper;
import com.pdev.stocktracker.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping
    public ResponseEntity<Stock> savePurchase(@RequestBody StockRequest request) {
        Pair<Stock, StockPurchase> stock = StockMapper.toStock(request);
        Stock savedStock = stockService.savePurchase(stock.getFirst(), stock.getSecond());
        return ResponseEntity.ok(savedStock);
    }

    @PostMapping("/add")
    public ResponseEntity<Stock> addPurchase(@RequestBody StockAddPurchaseRequest request) {
        try {
            Stock stock = stockService.addPurchase(request.getStockId(), StockMapper.toStockPurchase(request));
            return ResponseEntity.ok(stock);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Stock>> findAll() {
        return ResponseEntity.ok(stockService.findAll());
    }

}
