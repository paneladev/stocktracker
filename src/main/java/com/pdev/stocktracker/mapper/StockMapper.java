package com.pdev.stocktracker.mapper;

import com.pdev.stocktracker.controller.request.StockAddPurchaseRequest;
import com.pdev.stocktracker.controller.request.StockRequest;
import com.pdev.stocktracker.controller.response.StockPurchaseResponse;
import com.pdev.stocktracker.controller.response.StockResponse;
import com.pdev.stocktracker.entity.Stock;
import com.pdev.stocktracker.entity.StockPurchase;
import lombok.experimental.UtilityClass;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.util.Objects;

@UtilityClass
public class StockMapper {

    public static Pair<Stock, StockPurchase> toStock(StockRequest request) {

        final Stock stock = Stock.builder()
                .stock(request.getStock())
                .build();

        final StockPurchase stockPurchase = StockPurchase
                .builder()
                .date(request.getDate())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .build();

        return Pair.of(stock, stockPurchase);
    }

    public static StockPurchase toStockPurchase(StockAddPurchaseRequest request) {
        return StockPurchase.builder()
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .date(request.getDate())
                .build();
    }

    public static StockResponse toStockResponse(Stock stock) {
        final Long quantity = stock.getPurchases().stream().map(StockPurchase::getQuantity).reduce(0L, Long::sum);

        return StockResponse.builder()
                .id(stock.getId())
                .stock(stock.getStock())
                .price(stock.getPrice())
                .quantity(quantity)
                .priceTotal(Objects.nonNull(stock.getPrice()) ? stock.getPrice().multiply(BigDecimal.valueOf(quantity)) : BigDecimal.ZERO)
                .build();

    }

    public static StockPurchaseResponse stockDetailResponse(StockPurchase stockPurchase, String stock) {
        return StockPurchaseResponse.builder()
                .stock(stock)
                .date(stockPurchase.getDate())
                .quantity(stockPurchase.getQuantity())
                .price(stockPurchase.getPrice())
                .priceTotal(stockPurchase.getPrice().multiply(BigDecimal.valueOf(stockPurchase.getQuantity())))
                .build();
    }
}
