package com.pdev.stocktracker.repository;

import com.pdev.stocktracker.entity.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends MongoRepository<Stock, String> {
}
