package com.shinhan.zoomoney.stock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shinhan.zoomoney.member.MemberEntity;
import com.shinhan.zoomoney.stock.entity.StockHistoryEntity;

public interface StockHistoryRepository extends JpaRepository<StockHistoryEntity, Integer> {

	// 특정 회원의 거래 내역 삭제
	void deleteByMember(MemberEntity member);

	// 주식 매도 매입한 금액 계산
	@Query("SELECT SUM(sh.stockhistAmount) FROM StockHistoryEntity sh " +
			"WHERE sh.member.memberNum = :memberNum " +
			"AND sh.stock.stockId = :stockId " +
			"AND sh.stockhistType = :type")
	Integer getTotalStockAmount(@Param("memberNum") int memberNum,
			@Param("stockId") String stockId,
			@Param("type") String type);

	// 특정 회원이 보유한 주식별 개수, 평균 매수가격, 총 가치 조회
	@Query("SELECT s.stockName, s.stockId, " +
		       "SUM(CASE WHEN sh.stockhistType = '1' THEN sh.stockhistAmount ELSE 0 END) - " +
		       "SUM(CASE WHEN sh.stockhistType = '2' THEN sh.stockhistAmount ELSE 0 END) AS ownedAmount, " +
		       "AVG(CASE WHEN sh.stockhistType = '1' THEN sh.stockhistPrice ELSE null END) AS avgBuyPrice, " +
		       "MAX(sh.stockhistPrice) AS lastTradePrice, " +
		       "s.stockPrice " +
		       "FROM StockHistoryEntity sh " +
		       "JOIN sh.stock s " +
		       "WHERE sh.member.memberNum = :memberNum " +
		       "GROUP BY s.stockId, s.stockName, s.stockPrice")
		List<Object[]> getOwnedStocks(@Param("memberNum") int memberNum);

	List<StockHistoryEntity> findByMember_MemberNum(int memberNum);
	
	// 모든 히스토리를 시간순으로 불러오기
	@Query("SELECT sh FROM StockHistoryEntity sh WHERE sh.member.memberNum = :memberNum ORDER BY sh.stockHistDate ASC")
	List<StockHistoryEntity> findAllByMemberNumOrderByDate(@Param("memberNum") int memberNum);



}
