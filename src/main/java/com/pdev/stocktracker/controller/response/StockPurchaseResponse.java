package com.pdev.stocktracker.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class StockPurchaseResponse {

    private LocalDate date;
    private BigDecimal price;
    private long quantity;
    private BigDecimal priceTotal;

}
