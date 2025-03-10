package com.shinhan.zoomoney.stock;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockNewsRepository extends JpaRepository<StockEntity, Integer> {
    StockEntity findByStockName(String stockName);
}
