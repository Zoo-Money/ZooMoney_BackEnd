package com.shinhan.zoomoney.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shinhan.zoomoney.stock.entity.StockInfoEntity;

public interface StockInfoRepository extends JpaRepository<StockInfoEntity, Integer> {
    StockInfoEntity findByInfoNum(int infoNum);
}