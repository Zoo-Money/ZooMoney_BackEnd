package com.shinhan.zoomoney.stock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.shinhan.zoomoney.member.MemberEntity;
import com.shinhan.zoomoney.stock.entity.StockMoneyEntity;

public interface StockMoneyRepository extends JpaRepository<StockMoneyEntity, Integer> {
	// 특정 회원의 StockMoney 찾기
	Optional<StockMoneyEntity> findByMember(MemberEntity member);

	// 모든 멤버의 stockmoneyTotal을 업데이트
	@Modifying
	@Transactional
	@Query("UPDATE StockMoneyEntity s SET s.stockmoneyTotal = :amount")
	void updateAllStockMoneyTotal(@Param("amount") int amount);

	// 특정 회원의 잔고 존재 여부 확인
	// boolean existsByMemberNum(int memberNum);

	boolean existsByMember_MemberNum(int memberNum);
}
