package com.pdev.stocktracker.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stock_purchase")
public class StockPurchase {

    @Id
    private String id;

    private LocalDate date;

    private BigDecimal price;

    private Long quantity;
}
