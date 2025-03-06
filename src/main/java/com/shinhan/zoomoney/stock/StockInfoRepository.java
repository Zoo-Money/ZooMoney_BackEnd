package com.shinhan.zoomoney.stock;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockInfoRepository extends JpaRepository<StockInfoEntity, Integer> {
}