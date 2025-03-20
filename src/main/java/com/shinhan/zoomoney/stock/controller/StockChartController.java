package com.shinhan.zoomoney.stock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.zoomoney.stock.dto.StockDto;
import com.shinhan.zoomoney.stock.service.StockChartService;

@RestController
@RequestMapping("/stock")
public class StockChartController {

    private final StockChartService stockChartService;

    public StockChartController(StockChartService stockChartService) {
        this.stockChartService = stockChartService;
    }

    // API 연결 확인용
    @GetMapping("/chart")
    public ResponseEntity<List<Map<String, Object>>> getTop30Stock() {
        List<Map<String, Object>> stockData = stockChartService.getTopStocks();
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
                        (String) stock.get("hts_kor_isnm"), // 주식 종목명
                        (String) stock.get("mksc_shrn_iscd"), // 주식 코드
                        null, // stock_info (추후 추가 가능)
                        0
                ))
                .toList();

        stockChartService.saveStockList(stockDtoList);
        return ResponseEntity.ok("주식 데이터 저장 완료!");
    }

    // DB에 있는 주식 정보를 가져옴
    @GetMapping("/rank")
    public ResponseEntity<List<StockDto>> getStockList() {
        List<StockDto> stockList = stockChartService.getStockList();
        return ResponseEntity.ok(stockList);
    }

    // Stock 테이블 초기화 후, api와 Crawling한 데이터 DB에 저장
    @PutMapping("/getinfo")
    public ResponseEntity<List<StockDto>> updateStockTable() {
        return ResponseEntity.ok(stockChartService.updateStockTable());
    }

    // 특정 종목 코드(stockId)로 stock_info 가져오기
    @GetMapping("/info/{stockId}")
    public ResponseEntity<Map<String, String>> getStockInfoById(@PathVariable("stockId") String stockId) {
        String stockInfo = stockChartService.getStockInfoById(stockId);

        if (stockInfo != null) {
            Map<String, String> response = new HashMap<>();
            response.put("stock_info", stockInfo);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
   
}
