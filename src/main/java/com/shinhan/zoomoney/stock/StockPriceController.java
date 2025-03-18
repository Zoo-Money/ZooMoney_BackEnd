package com.shinhan.zoomoney.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Controller
@RestController
public class StockPriceController {

    @Value("${stock.api.key}")
    private String appKey;

    @Value("${stock.api.secret}")
    private String secretKey;

    @Autowired
    StockChartTokenService tokenService;

    @Autowired
    StockClosingPrice stockClosingPrice;
    @Autowired
    private RestTemplate restTemplate;

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
