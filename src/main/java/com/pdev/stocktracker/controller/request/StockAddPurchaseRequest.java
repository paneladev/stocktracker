package com.pdev.stocktracker.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
public class StockAddPurchaseRequest {

    private String stockId;
    private long quantity;
    private LocalDate date;
    private BigDecimal price;
}
