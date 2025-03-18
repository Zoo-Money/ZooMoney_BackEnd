package com.shinhan.zoomoney.stock;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockHistoryRepository extends JpaRepository<StockHistoryEntity, Integer> {

	List<StockHistoryEntity> findByMember_MemberNum(Integer memberNum);
}
