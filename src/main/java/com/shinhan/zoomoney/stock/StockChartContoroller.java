package com.shinhan.zoomoney.stock;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockChartContoroller {
	
	private final StockChartService stockChartService;
	
	public StockChartContoroller(StockChartService stockChartService) {
		this.stockChartService = stockChartService;
	}
	
	@GetMapping("/chart")
	public ResponseEntity<List<Map<String,Object>>> getTop30Stock(){
		List<Map<String, Object>> stockData = stockChartService.getTopStocks();
		System.out.println("실행");
		return ResponseEntity.ok(stockData);
	}
	
	
	// 주식 데이터를 API에서 가져와 DB에 저장
    @PostMapping("/save")
    public ResponseEntity<String> saveStockData() {
        List<Map<String, Object>> stockList = stockChartService.getTopStocks();
        
        // 데이터를 `StockDto`로 변환
        List<StockDto> stockDtoList = stockList.stream()
                .map(stock -> new StockDto(
                        null, // stockNum (자동 생성)
                        (String) stock.get("hts_kor_isnm"),  // 주식 종목명
                        (String) stock.get("mksc_shrn_iscd"), // 주식 코드
                        null // stock_info (추후 추가 가능)
                ))
                .toList();

        stockChartService.saveStockList(stockDtoList);
        return ResponseEntity.ok("주식 데이터 저장 완료!");
    }
}
