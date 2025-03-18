package com.shinhan.zoomoney.stock;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shinhan.zoomoney.member.MemberEntity;

public interface StockHistoryRepository extends JpaRepository<StockHistoryEntity, Integer>{
	
	// 특정 회원의 거래 내역 삭제
	void deleteByMember(MemberEntity member);
	
	// 주식 매도 매입한 금액 계산
	 @Query("SELECT SUM(sh.stockhistAmount) FROM StockHistoryEntity sh " +
	           "WHERE sh.member.memberNum = :memberNum " +
	           "AND sh.stock.stockNum = :stockNum " +
	           "AND sh.stockhistType = :type")
	    Integer getTotalStockAmount(@Param("memberNum") int memberNum, 
	                                @Param("stockNum") int stockNum, 
	                                @Param("type") String type);
	 
	// 특정 회원이 보유한 주식별 개수, 평균 매수가격, 총 가치 조회
	    @Query("SELECT sh.stock.stockName, " +
	           "       (SUM(CASE WHEN sh.stockhistType = '1' THEN sh.stockhistAmount ELSE 0 END) - " +
	           "        SUM(CASE WHEN sh.stockhistType = '2' THEN sh.stockhistAmount ELSE 0 END)) AS quantity, " +
	           "       COALESCE(SUM(CASE WHEN sh.stockhistType = '1' THEN sh.stockhistPrice * sh.stockhistAmount ELSE 0 END) / " +
	           "       NULLIF(SUM(CASE WHEN sh.stockhistType = '1' THEN sh.stockhistAmount ELSE 0 END), 0), 0) AS averagePrice, " +
	           "       sh.stock.stockPrice, " +  // 현재 주가
	           "       (SELECT sh2.stockhistPrice FROM StockHistoryEntity sh2 " +
	           "        WHERE sh2.stock.stockNum = sh.stock.stockNum " +
	           "        ORDER BY sh2.stockHistDate DESC LIMIT 1) AS lastTradePrice " + // 최근 거래 가격
	           "FROM StockHistoryEntity sh " +
	           "WHERE sh.member.memberNum = :memberNum " +
	           "GROUP BY sh.stock.stockName, sh.stock.stockPrice " +
	           "HAVING (SUM(CASE WHEN sh.stockhistType = '1' THEN sh.stockhistAmount ELSE 0 END) - " +
	           "        SUM(CASE WHEN sh.stockhistType = '2' THEN sh.stockhistAmount ELSE 0 END)) > 0")
	    List<Object[]> getOwnedStocks(@Param("memberNum") int memberNum);
	    List<StockHistoryEntity> findByMember_MemberNum(Integer memberNum);

}
