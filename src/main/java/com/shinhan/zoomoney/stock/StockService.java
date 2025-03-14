package com.shinhan.zoomoney.stock;

import java.beans.Transient;
import java.util.Date;
import java.util.Optional;

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
	
	public StockService(StockMoneyRepository stockMoneyRepository,
			StockHistoryRepository stockHistoryRepository,
			StockChartRepository stockChartRepository,
			MemberRepository memberRepository) {
		this.stockMoneyRepository = stockMoneyRepository;
		this.stockHistoryRepository = stockHistoryRepository;
		this.stockChartRepository = stockChartRepository;
		this.memberRepository = memberRepository;
	}
	
	
	// 매수
	@Transactional
	public String buyStock(int memberNum, int stockNum, int amount, int price) {
		
		// member 잔액 조회
		Optional<StockMoneyEntity> stockMoneyMem = stockMoneyRepository.findByMemberNum(memberNum);
		if(stockMoneyMem.isEmpty()) {
			return "사용자의 잔액 정보를 찾을 수 없습니다.";
		}
		
		StockMoneyEntity stockMoney = stockMoneyMem.get();
		int totalPrice = amount * price;
		
		// 잔액 확인 후 차감
		if(stockMoney.getStockmoneyTotal()<totalPrice) {
			return "잔액이 부족합니다.";
		}
		
		stockMoney.setStockmoneyTotal(stockMoney.getStockmoneyTotal() - totalPrice);
		stockMoneyRepository.save(stockMoney);
		
		// 매수한 주식 정보 가져오기
		Optional<StockEntity> stockOpt = stockChartRepository.findById(memberNum);
		if (stockOpt.isEmpty()) {
            return "해당 주식 정보를 찾을 수 없습니다.";
        }
		
		// 회원 정보 가져오기
		Optional<MemberEntity> memberOpt = memberRepository.findById(memberNum);
		if(memberOpt.isEmpty()) {
			return "회원 정보를 찾을 수 없습니다.";
		}
		
		// 매수 기록 저장
		StockHistoryEntity stockHistory = StockHistoryEntity.builder()
				.member(memberOpt.get())
				.stock(stockOpt.get())
				.stockhistType("1")
                .stockhistAmount(amount)
                .stockhistPrice(price)
                .stockHistDate(new Date())
				.build();
		
		stockHistoryRepository.save(stockHistory);
		
		return "매수 완료";
	}
	
}
