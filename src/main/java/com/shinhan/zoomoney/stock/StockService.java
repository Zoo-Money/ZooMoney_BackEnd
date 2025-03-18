package com.shinhan.zoomoney.stock;

import java.beans.Transient;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shinhan.zoomoney.member.MemberEntity;
import com.shinhan.zoomoney.member.MemberRepository;

@Service
public class StockService {
	private final StockMoneyRepository stockMoneyRepository;
	private final StockHistoryRepository stockHistoryRepository;
	private final StockChartRepository stockChartRepository;
	private final MemberRepository memberRepository;

	public StockService(StockMoneyRepository stockMoneyRepository, StockHistoryRepository stockHistoryRepository,
			StockChartRepository stockChartRepository, MemberRepository memberRepository) {
		this.stockMoneyRepository = stockMoneyRepository;
		this.stockHistoryRepository = stockHistoryRepository;
		this.stockChartRepository = stockChartRepository;
		this.memberRepository = memberRepository;
	}

	// 매수
	@Transactional
	public String buyStock(int memberNum, int stockNum, int amount, int price) {

		int totalPrice = amount * price;
		StockMoneyEntity stockMoney = stockMoneyRepository.findById(memberNum).orElse(null);

		// 잔액 확인 후 차감
		if (stockMoney.getStockmoneyTotal() < totalPrice) {
			return "잔액이 부족합니다.";
		}

		stockMoney.setStockmoneyTotal(stockMoney.getStockmoneyTotal() - totalPrice);
		

		// 매수한 주식 정보 가져오기
		StockEntity stock = stockChartRepository.findById(stockNum).orElse(null);
		if (stock == null) {
			return "해당 주식 정보를 찾을 수 없습니다.";
		}
 
		// 매수 기록 저장
		MemberEntity member = MemberEntity.builder().memberNum(memberNum).build();
		StockHistoryEntity stockHistory = StockHistoryEntity.builder()
				.member(member)
				.stock(stock)
				.stockhistType("1")
				.stockhistAmount(amount)
				.stockhistPrice(price)
				.stockHistDate(new Date()).build();

		
		stockMoneyRepository.save(stockMoney);
		stockHistoryRepository.save(stockHistory);

		return "매수 완료";
	}
	
	 // 매도
	 public String sellStock(int memberNum, int stockNum, int amount, int price) {
		 // 회원 정보 가져오기
		 MemberEntity member = memberRepository.findById(memberNum)
				 .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
		 
		 // 주식 정보 가져오기
		 StockEntity stock = stockChartRepository.findById(stockNum)
				 .orElseThrow(() -> new IllegalArgumentException("해당 주식을 찾을 수 없습니다."));
		 
		 // 사용자의 보유 주식 계산
		 Integer totalBuyAmount = stockHistoryRepository.getTotalStockAmount(memberNum, stockNum, "1");
		 Integer totalSellAmount = stockHistoryRepository.getTotalStockAmount(memberNum, stockNum, "2");
		 
		 int ownedAmount = (totalBuyAmount != null ? totalBuyAmount : 0) - (totalSellAmount != null ? totalSellAmount : 0);

	        if (ownedAmount < amount) {
	            return "보유 주식이 부족합니다.";
	        }
	        
	     // 매도 금액 계산
	     int totalSellPrice = amount * price;
	     
	     // 사용자의 StockMoney 총액 업데이트
	     StockMoneyEntity stockMoney = stockMoneyRepository.findById(memberNum)
	    		 .orElseThrow(() -> new IllegalArgumentException("해당 회원의 StockMoney 정보가 없습니다."));
		 stockMoney.setStockmoneyTotal(stockMoney.getStockmoneyTotal() + totalSellPrice);
		 stockMoneyRepository.save(stockMoney);
		 
		 // 메도 거래 추가
		 StockHistoryEntity stockHistory = StockHistoryEntity.builder()
	                .member(member)
	                .stock(stock)
	                .stockhistType("2")  // "2"는 매도
	                .stockhistAmount(amount)
	                .stockhistPrice(price)
	                .stockHistDate(new Date())
	                .build();
	        stockHistoryRepository.save(stockHistory);
		 return "매도 완료";
	 }
	 
	// 사용자의 보유 주식 정보 조회
	 @Transactional(readOnly = true)
	    public List<OwnedStockDto> getOwnedStocksByMember(int memberNum) {
	        List<Object[]> resultList = stockHistoryRepository.getOwnedStocks(memberNum);

	        return resultList.stream()
	                .map(obj -> new OwnedStockDto(
	                		(String) obj[0],   // 주식명
	                        ((Number) obj[1]).intValue(),  // 보유 주식 수량
	                        ((Number) obj[2]).doubleValue(), // 평균 매수 가격
	                        ((Number) obj[1]).intValue() * ((Number) obj[3]).doubleValue(), // 총 가치 (보유량 * 현재 주가)
	                        ((Number) obj[3]).intValue(),  // 현재 주가
	                        ((Number) obj[4]).intValue()   // 최근 거래 가격
	                        
	                ))
	                .collect(Collectors.toList());
	    }
	 
	 
	// 모든 멤버에게 1000000원 충전
	@Transactional
	public void chargeAllMembers() {
		stockMoneyRepository.updateAllStockMoneyTotal(1000000);
	}

	// StockMoney 초기화할 시 StockHistory도 초기화
	@Transactional
	public String resetStockMoney(int memberNum) {
		// 해당 회원이 존재하는지 확인
        String message = null;
		MemberEntity member = memberRepository.findById(memberNum)
				.orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

		// StockMoneyEntity 조회 (JPA 관리 상태 유지)
		StockMoneyEntity stockMoney = stockMoneyRepository.findByMember(member).orElse(null);
		
		stockHistoryRepository.deleteByMember(member);
		
		if (stockMoney != null) {
			// 존재하면 Dirty Checking 활용하여 값 변경		 
			stockMoney.setStockmoneyTotal(1000000);
			message =  "기존 StockMoney 초기화 완료 (100만원)";
		} else {
			// 존재하지 않으면 새로운 엔티티 생성 후 저장
			stockMoney = 
					StockMoneyEntity.builder().member(member) // ID를 직접 설정하면 안 됨 (PK 충돌																							// 방지)
					.stockmoneyTotal(1000000).build();
			
			message= "새로운 StockMoney 생성 완료 (100만원)";
		}
		stockMoneyRepository.save(stockMoney);
		return message;
	}

	// 특정 회원의 잔고 조회
	public int getStockMoney(int memberNum) {
		Optional<StockMoneyEntity> stockMoney = stockMoneyRepository.findById(memberNum);
		return stockMoney.map(StockMoneyEntity::getStockmoneyTotal).orElse(0);
	}
	

}
