package com.shinhan.zoomoney.stock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shinhan.zoomoney.stock.entity.StockHistoryEntity;

public interface StockHistoryBackupRepository extends JpaRepository<StockHistoryEntity, Integer>{
	List<StockHistoryEntity> findByMember_MemberNum(int memberNum);
}
