package com.shinhan.zoomoney.stock;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stocks")
public class StockChartContoroller {
	
	private final StockChartService stockChartService;
	
	public StockChartContoroller(StockChartService stockChartService) {
		this.stockChartService = stockChartService;
	}
	
	@GetMapping("/marketcap")
	public ResponseEntity<List<Map<String,Object>>> getTop30Stock(){
		List<Map<String, Object>> stockData = stockChartService.getTop30MarketCapStocks();
		System.out.println("실행");
		return ResponseEntity.ok(stockData);
	}

}
