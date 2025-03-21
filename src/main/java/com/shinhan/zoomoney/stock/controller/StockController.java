package com.shinhan.zoomoney.stock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.zoomoney.stock.dto.OwnedStockDto;
import com.shinhan.zoomoney.stock.dto.StockMoneyDto;
import com.shinhan.zoomoney.stock.service.StockMoneyService;
import com.shinhan.zoomoney.stock.service.StockService;

import lombok.Data;

@RestController
@RequestMapping("/stock")
public class StockController {
	private final StockService stockService;
	@Autowired
	StockMoneyService stockMoneyService;

	public StockController(StockService stockService) {
		this.stockService = stockService;
	}

	// 매수
	@PostMapping("/buy")
	public ResponseEntity<String> buyStock(@RequestBody BuyDto buyDto) {
		System.out.println(buyDto);
		String result = stockService.buyStock(buyDto.memberNum, buyDto.stockId,
				buyDto.amount, buyDto.price);
		return ResponseEntity.ok(result);
	}

	// 매도
	@PostMapping("/sell")
	public ResponseEntity<String> sellStock(
			@RequestBody SellDto sellDto) {
		String result = stockService.sellStock(sellDto.memberNum, sellDto.stockId, sellDto.amount, sellDto.price);
		System.out.println("Received Sell Request: " + sellDto);
		return ResponseEntity.ok(result);
	}

	// 돈 충전
	@PostMapping("/chargemoney")
	public ResponseEntity<String> chargeAllStockMoney() {
		stockService.chargeAllMembers();
		return ResponseEntity.ok("모든 멤버에게 1000000원 충전 완료!");
	}

	@PostMapping("/userStatus")
	public ResponseEntity<Integer> checkChildNum(@RequestBody StockMoneyDto stockMoneyDto) {
		int memberNum = stockMoneyDto.getMemberNum();
		boolean exists = stockMoneyService.hasMemberNumCheck(memberNum);
		int resultCode = exists ? 1 : 0;
		return ResponseEntity.ok(resultCode);
	}

	// 주식 시작하기 버튼 클릭 -> StockMoney 테이블에 member와 StockMoney추가
	@PostMapping("/start")
	public ResponseEntity<String> startStock(@RequestBody StockMoneyDto stockMoneyDto) {
		int memberNum = stockMoneyDto.getMemberNum();

		try {
			String result = stockService.resetStockMoney(memberNum);
			return ResponseEntity.ok(result);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 보유한 주식 내역 조회
	@GetMapping("/owned")
	public ResponseEntity<List<OwnedStockDto>> getOWnedStock(@RequestParam("memberNum") int memberNum) {
		List<OwnedStockDto> result = stockService.getOwnedStocksByMember(memberNum);
		return ResponseEntity.ok(result);
	}
	
	// 보유한 예수금(현금) 조회
	@GetMapping("/getmoney")
	public ResponseEntity<Integer> getStockMoney(@RequestParam("memberNum") int memberNum){
		int stockMoney = stockService.getStockMoney(memberNum);
		
		return ResponseEntity.ok(stockMoney);
	}
}

@Data
class BuyDto {
	int memberNum;
	String stockId;
	int amount;
	int price;
}

@Data
class SellDto {
	int memberNum;
	String stockId;
	int amount;
	int price;
}