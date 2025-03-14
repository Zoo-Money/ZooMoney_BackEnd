package com.shinhan.zoomoney.stock;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMoneyRepository extends JpaRepository<StockMoneyEntity,Integer> {
	Optional<StockMoneyEntity> findByMemberNum(int memberNum);
}
