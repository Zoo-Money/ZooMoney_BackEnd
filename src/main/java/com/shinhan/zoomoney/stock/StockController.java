package com.shinhan.zoomoney.stock;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockController {
	private final StockService stockService;
	
	public StockController(StockService stockService) {
		this.stockService = stockService;
	}
	
	// 매도
	@PostMapping("/buy")
	public ResponseEntity<String> buyStock(
			@RequestParam(name="memberNum") int memberNum,
			@RequestParam(name="stockNum") int stockNum,
			@RequestParam(name="amount") int amount,
			@RequestParam(name="price") int price){
		String result = stockService.buyStock(memberNum, stockNum, amount, price);
		return ResponseEntity.ok(result);
	}
	
	// 돈 충전
	
}
