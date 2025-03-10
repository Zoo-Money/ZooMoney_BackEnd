package com.shinhan.zoomoney.stock;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface StockChartRepository extends JpaRepository<StockEntity, Integer>{
	Optional<StockEntity> findByStockId(String stock_id);
	

}
