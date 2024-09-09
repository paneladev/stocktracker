package com.pdev.stocktracker.client.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"regularMarketPrice"})
public class BrapiStockDataResponse implements Serializable {

    private String symbol;
    private String currency;
    private String shortName;
    private String longName;
    private Double regularMarketPrice;
    private Double regularMarketDayHigh;
    private Double regularMarketDayLow;
    private Double regularMarketPreviousClose;
    private String logourl;
}
