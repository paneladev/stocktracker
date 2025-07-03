package com.pdev.stocktracker.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class StockPurchaseResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stock;
    private LocalDate date;
    private BigDecimal price;
    private long quantity;
    private BigDecimal priceTotal;

}
