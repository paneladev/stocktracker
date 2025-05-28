package com.pdev.stocktracker.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
public class StockAddPurchaseRequest {

    @NotBlank
    private String stockId;
    @NotNull
    private long quantity;
    @NotNull
    private LocalDate date;
    @NotNull
    private BigDecimal price;
}
