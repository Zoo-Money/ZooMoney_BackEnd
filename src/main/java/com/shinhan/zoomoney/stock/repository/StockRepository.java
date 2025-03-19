package com.shinhan.zoomoney.stock.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shinhan.zoomoney.stock.entity.StockEntity;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Integer> {
    // 모든 종목의 stockId를 가져오는 쿼리 메서드
    @Query("SELECT s.stockId FROM StockEntity s")
    List<String> findAllStockIds();

    // stockId로 StockEntity를 조회하는 메서드
    Optional<StockEntity> findByStockId(String stockId);
}
