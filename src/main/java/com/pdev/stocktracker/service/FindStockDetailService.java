package com.pdev.stocktracker.service;

import com.pdev.stocktracker.client.BrapiClient;
import com.pdev.stocktracker.client.response.BrapiStockDataResponse;
import com.pdev.stocktracker.client.response.BrapiStockResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindStockDetailService {

    @Value("${stock.client.brapi.token}")
    private String token;

    private final BrapiClient brapiClient;

    @Cacheable(value = "acao", key="#stock")
    public Optional<BrapiStockDataResponse> getBrapiStockDetail(String stock) {
        log.info("Consultando informações da ação: {} na Brapi", stock);
        try {
            BrapiStockResponse brapiStockResponse = brapiClient.getStock(stock, token);

            log.info("Retorno Brapi da ação {}: {}", stock, brapiStockResponse.getResults().get(0));
            return Optional.of(brapiStockResponse.getResults().get(0));

        } catch (FeignException e) {
            log.error("Erro ao consultar a ação {}: {}", stock, e.getMessage());
            return Optional.empty();
        } catch (Exception ex) {
            log.error("Erro desconhecido ao consultar a ação: {} na Brapi. {}", stock, ex.getMessage());
            return Optional.empty();
        }
    }
}
