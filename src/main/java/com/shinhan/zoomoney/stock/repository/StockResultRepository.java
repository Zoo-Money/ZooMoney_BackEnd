package com.shinhan.zoomoney.stock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shinhan.zoomoney.stock.entity.StockResultEntity;

public interface StockResultRepository extends JpaRepository<StockResultEntity, Integer> {
	List<StockResultEntity> findByMember_MemberNum(int memberNum);
}
