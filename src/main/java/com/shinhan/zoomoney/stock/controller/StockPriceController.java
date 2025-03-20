package com.shinhan.zoomoney.stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.zoomoney.stock.service.StockChartTokenService;
import com.shinhan.zoomoney.stock.service.StockClosingPriceService;

@RestController
public class StockPriceController {
    @Autowired
    StockChartTokenService tokenService;

    @Autowired
    StockClosingPriceService stockClosingPrice;

    // 모든 종목에 대해 종가를 가져와서 DB에 업데이트하는 엔드포인트
    @PostMapping("/updateStockPrices")
    public ResponseEntity<String> updateStockPrices() {
        String accessToken = tokenService.getAccessToken();
        try {
            // 모든 종목에 대해 종가 업데이트
            stockClosingPrice.fetchAndUpdateStockPricesForAllStocks(accessToken);
            return ResponseEntity.ok("Stock prices updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update stock prices.");
        }
    }
}
