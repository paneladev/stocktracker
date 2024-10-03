package com.pdev.stocktracker.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class StockResponse {

    private String id;
    private String stock;
    private BigDecimal price;
    private Long quantity;
    private BigDecimal priceTotal;
}
