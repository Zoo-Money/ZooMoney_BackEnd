package com.shinhan.zoomoney.stock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shinhan.zoomoney.stock.entity.StockHistoryBackupEntity;

public interface StockHistoryBackupRepository extends JpaRepository<StockHistoryBackupEntity, Integer>{
	List<StockHistoryBackupEntity> findByMember_MemberNum(int memberNum);
}
