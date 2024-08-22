package com.pdev.stocktracker.repository;

import com.pdev.stocktracker.entity.StockPurchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockPurchaseRepository extends MongoRepository<StockPurchase, String> {
}
