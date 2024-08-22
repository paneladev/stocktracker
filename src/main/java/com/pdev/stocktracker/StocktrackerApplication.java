package com.pdev.stocktracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class StocktrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StocktrackerApplication.class, args);
    }

}
