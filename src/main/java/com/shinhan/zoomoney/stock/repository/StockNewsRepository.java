package com.shinhan.zoomoney.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shinhan.zoomoney.stock.entity.StockEntity;

public interface StockNewsRepository extends JpaRepository<StockEntity, Integer> {
    StockEntity findByStockName(String stockName);
}
