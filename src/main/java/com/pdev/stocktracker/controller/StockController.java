package com.pdev.stocktracker.controller;

import com.pdev.stocktracker.controller.request.StockAddPurchaseRequest;
import com.pdev.stocktracker.controller.request.StockRequest;
import com.pdev.stocktracker.controller.response.StockPurchaseResponse;
import com.pdev.stocktracker.controller.response.StockResponse;
import com.pdev.stocktracker.entity.Stock;
import com.pdev.stocktracker.entity.StockPurchase;
import com.pdev.stocktracker.mapper.StockMapper;
import com.pdev.stocktracker.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StockResponse> savePurchase(@Valid @RequestBody StockRequest request) {
        Pair<Stock, StockPurchase> stock = StockMapper.toStock(request);
        Stock savedStock = stockService.saveStock(stock.getFirst(), stock.getSecond());
        return ResponseEntity.status(HttpStatus.CREATED).body(StockMapper.toStockResponse(savedStock));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StockPurchaseResponse> addPurchase(@Valid @RequestBody StockAddPurchaseRequest request) {
        StockPurchase stockPurchase = stockService.addPurchase(request.getStockId(), StockMapper.toStockPurchase(request));
        return ResponseEntity.ok(StockMapper.stockDetailResponse(stockPurchase, null));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<StockResponse>> getStocks() {
        final List<Stock> stocks = stockService.findAll();
        List<StockResponse> response = stocks.stream()
                .map(StockMapper::toStockResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/detail/{stockId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<StockPurchaseResponse>> getPurchasesByStockId(@PathVariable String stockId) {
        Stock stock = stockService.findById(stockId);
        List<StockPurchaseResponse> stockPurchaseResponseList = stock.getPurchases().stream()
                .map(purchase -> StockMapper.stockDetailResponse(purchase, stock.getStock()))
                .toList();
        return ResponseEntity.ok(stockPurchaseResponseList);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("{stockId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteStock(@PathVariable String stockId) {
        Stock stock = stockService.findById(stockId);
        stockService.delete(stock);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
