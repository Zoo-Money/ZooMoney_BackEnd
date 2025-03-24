package com.shinhan.zoomoney.stock.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shinhan.zoomoney.member.MemberEntity;
import com.shinhan.zoomoney.member.MemberRepository;
import com.shinhan.zoomoney.stock.dto.OwnedStockDto;
import com.shinhan.zoomoney.stock.entity.StockEntity;
import com.shinhan.zoomoney.stock.entity.StockHistoryEntity;
import com.shinhan.zoomoney.stock.entity.StockMoneyEntity;
import com.shinhan.zoomoney.stock.repository.StockChartRepository;
import com.shinhan.zoomoney.stock.repository.StockHistoryRepository;
import com.shinhan.zoomoney.stock.repository.StockMoneyRepository;

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
	public String buyStock(int memberNum, String stockId, int amount, int price) {

		int totalPrice = amount * price;
		StockMoneyEntity stockMoney = stockMoneyRepository.findById(memberNum).orElse(null);

		// 잔액 확인 후 차감
		if (stockMoney.getStockmoneyTotal() < totalPrice) {
			return "잔액이 부족합니다.";
		}

		stockMoney.setStockmoneyTotal(stockMoney.getStockmoneyTotal() - totalPrice);

		// 매수한 주식 정보 가져오기
		StockEntity stock = stockChartRepository.findByStockId(stockId).orElse(null);
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
	public String sellStock(int memberNum, String stockId, int amount, int price) {
		// 회원 정보 가져오기
		MemberEntity member = memberRepository.findById(memberNum)
				.orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

		// 주식 정보 가져오기
		StockEntity stock = stockChartRepository.findByStockId(stockId)
				.orElseThrow(() -> new IllegalArgumentException("해당 주식을 찾을 수 없습니다."));

		// 사용자의 보유 주식 계산
		Integer totalBuyAmount = stockHistoryRepository.getTotalStockAmount(memberNum, stockId, "1");
		Integer totalSellAmount = stockHistoryRepository.getTotalStockAmount(memberNum, stockId, "2");

		int ownedAmount = (totalBuyAmount != null ? totalBuyAmount : 0)
				- (totalSellAmount != null ? totalSellAmount : 0);

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
				.stockhistType("2") // "2"는 매도
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
		// 1. 모든 히스토리를 시간순으로 불러오기
	    List<StockHistoryEntity> allHistory = stockHistoryRepository.findAllByMemberNumOrderByDate(memberNum);

	    // 2. 종목별로 그룹핑
	    Map<String, List<StockHistoryEntity>> grouped = allHistory.stream()
	        .collect(Collectors.groupingBy(h -> h.getStock().getStockId()));

	    List<OwnedStockDto> result = new ArrayList<>();

	    for (String stockId : grouped.keySet()) {
	        List<StockHistoryEntity> historyList = grouped.get(stockId);
	        String stockName = historyList.get(0).getStock().getStockName();
	        int currentPrice = historyList.get(0).getStock().getStockPrice();

	        // 현재 보유 수량 및 평균 매입가 계산
	        int ownedAmount = 0;
	        int costSum = 0;

	        for (StockHistoryEntity hist : historyList) {
	            if ("1".equals(hist.getStockhistType())) { // 매수
	                ownedAmount += hist.getStockhistAmount();
	                costSum += hist.getStockhistAmount() * hist.getStockhistPrice();
	            } else if ("2".equals(hist.getStockhistType())) { // 매도
	                // 매도 수량만큼 원가 차감
	                int sellAmount = hist.getStockhistAmount();

	                while (sellAmount > 0 && ownedAmount > 0) {
	                    // 가장 최근 매수 내역부터 차감 (단순화된 FIFO는 아님)
	                    // 정확한 FIFO 원한다면 Queue 사용 필요
	                    int reduce = Math.min(sellAmount, ownedAmount);
	                    costSum -= reduce * hist.getStockhistPrice();  // 단순 처리
	                    ownedAmount -= reduce;
	                    sellAmount -= reduce;
	                }
	            }
	        }

	        if (ownedAmount <= 0) continue; // 보유 수량이 0 이하이면 추가하지 않음

	        double avgPrice = (double) costSum / ownedAmount;
	        int lastPrice = historyList.get(historyList.size() - 1).getStockhistPrice();

	        result.add(new OwnedStockDto(
	            stockName,
	            stockId,
	            ownedAmount,
	            avgPrice,
	            ownedAmount * currentPrice,
	            lastPrice,
	            currentPrice
	        ));
	    }

	    return result;
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
			message = "기존 StockMoney 초기화 완료 (100만원)";
		} else {
			// 존재하지 않으면 새로운 엔티티 생성 후 저장
			stockMoney = StockMoneyEntity.builder().member(member) // ID를 직접 설정하면 안 됨 (PK 충돌 // 방지)
					.stockmoneyTotal(1000000).build();

			message = "새로운 StockMoney 생성 완료 (100만원)";
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
