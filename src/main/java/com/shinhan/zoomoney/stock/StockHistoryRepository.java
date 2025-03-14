package com.shinhan.zoomoney.stock;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockHistoryRepository extends JpaRepository<StockHistoryEntity, Integer>{
	
}
