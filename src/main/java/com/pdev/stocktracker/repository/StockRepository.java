package com.pdev.stocktracker.repository;

import com.pdev.stocktracker.entity.Stock;
import com.pdev.stocktracker.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends MongoRepository<Stock, String> {

    List<Stock> findByUser(User user);

    Optional<Stock> findByIdAndUserId(String id, String userId);

    Optional<Stock> findByStockAndUserId(String stock, String userId);
}
