package com.shinhan.zoomoney.stock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shinhan.zoomoney.stock.entity.StockEntity;

public interface StockChartRepository extends JpaRepository<StockEntity, Integer> {
    Optional<StockEntity> findByStockId(String stock_id);

    // 중복 저장 방지
    boolean existsByStockId(String stockId);

    // 특정 종목 코드 (`stockId`)를 가지고 있으면서 stock_num이 31~60 사이에 있는 데이터 찾기
    @Query("SELECT s FROM StockEntity s WHERE s.stockId = :stockId AND s.stockNum BETWEEN 31 AND 60")
    Optional<StockEntity> findStockInfoInRange(@Param("stockId") String stockId);

    // 특정 종목 코드(stock_id)에 해당하는 stock_info만 조회
    @Query("SELECT s.stockInfo FROM StockEntity s WHERE s.stockId = :stockId")
    String findStockInfoByStockId(@Param("stockId") String stockId);

}
