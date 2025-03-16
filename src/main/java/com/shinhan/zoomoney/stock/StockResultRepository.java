package com.shinhan.zoomoney.stock;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockResultRepository 
	extends JpaRepository<StockResultEntity, Integer>{
	List<StockResultEntity> findByMember_MemberNum(Integer memberNum);
}
