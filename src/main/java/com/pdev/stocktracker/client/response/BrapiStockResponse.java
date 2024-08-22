package com.pdev.stocktracker.client.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrapiStockResponse {

    private List<BrapiStockDataResponse> results;
}
