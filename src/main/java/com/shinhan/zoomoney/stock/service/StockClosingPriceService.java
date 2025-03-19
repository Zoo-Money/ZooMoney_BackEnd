package com.shinhan.zoomoney.stock.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.zoomoney.stock.entity.StockEntity;
import com.shinhan.zoomoney.stock.repository.StockRepository;

@Service
public class StockClosingPriceService {
    @Autowired
    StockRepository stockRepository;

    @Value("${stock.api.key}")
    private String appKey;

    @Value("${stock.api.secret}")
    private String secretKey;

    public void fetchAndUpdateStockPricesForAllStocks(String accessToken) {
        // DB에서 모든 종목의 ID를 가져옴
        List<String> stockIds = getStockIds();

        // 모든 종목에 대해 API 요청
        for (String stockId : stockIds) {
            System.out.println("Fetching stock price for: " + stockId);
            fetchAndUpdateStockPrices(accessToken, stockId);

            try {
                TimeUnit.SECONDS.sleep(1);// 1초 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> getStockIds() {
        // DB에서 종목 ID 목록 가져옴
        return stockRepository.findAllStockIds();
    }

    public void fetchAndUpdateStockPrices(String accessToken, String stockId) {
        Map<String, Object> stockPrices = getStockPrice(accessToken, stockId); // API 호출로 종가 값 받아오기

        // stck_prdy_clpr 값만 가져와서 DB 업데이트
        if (stockPrices != null && !stockPrices.isEmpty()) {
            String stockIdFromApi = (String) stockPrices.get("stck_shrn_iscd");
            int newStockPrice = Integer.parseInt((String) stockPrices.get("stck_prdy_clpr"));

            // DB에서 종목코드로 해당 종목을 찾음
            Optional<StockEntity> existingStock = stockRepository.findByStockId(stockIdFromApi);

            if (existingStock.isPresent()) {
                StockEntity stockEntity = existingStock.get();

                // DB에 저장된 종가와 새로 가져온 종가가 다르면 업데이트
                if (stockEntity.getStockPrice() != newStockPrice) {
                    stockEntity.setStockPrice(newStockPrice); // 종가 업데이트
                    stockRepository.save(stockEntity); // DB에 저장
                    System.out.println("Updated stock price for: " + stockIdFromApi);
                } else {
                    System.out.println("No update required for: " + stockIdFromApi);
                }
            } else {
                System.out.println("Stock not found in DB for ID: " + stockIdFromApi);
            }
        }
    }

    private Map<String, Object> getStockPrice(String accessToken, String stockId) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://openapi.koreainvestment.com:9443")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .defaultHeader("authorization", "Bearer " + accessToken)
                .defaultHeader("appkey", appKey)
                .defaultHeader("appsecret", secretKey)
                .defaultHeader("tr_id", "FHKST03010100")
                .defaultHeader("custtype", "P")
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .build();

        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice")
                        .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                        .queryParam("FID_INPUT_ISCD", stockId)
                        .queryParam("FID_INPUT_DATE_1", "20240311")
                        .queryParam("FID_INPUT_DATE_2", "20250311")
                        .queryParam("FID_PERIOD_DIV_CODE", "D")
                        .queryParam("FID_ORG_ADJ_PRC", "1")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("Response: " + response);
        ObjectMapper objectMapper = new ObjectMapper(); // { output1 : {stck_prdy_clpr : 44444} }
        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> output1 = (Map<String, Object>) map.get("output1");
        int stckPrdyClpr = Integer.parseInt((String) output1.get("stck_prdy_clpr"));
        System.out.println("전일 종가 (stck_prdy_clpr): " + stckPrdyClpr);
        updateStockPriceInDB(stockId, stckPrdyClpr);
        return null;
    }

    private void updateStockPriceInDB(String stockId, int closingPrice) {
        // DB에서 stockId에 해당하는 종목을 찾음
        Optional<StockEntity> existingStock = stockRepository.findByStockId(stockId);

        if (existingStock.isPresent()) {
            StockEntity stockEntity = existingStock.get();
            // 종가 값 업데이트
            stockEntity.setStockPrice(closingPrice);
            stockRepository.save(stockEntity);
            System.out.println("Stock price updated in DB: " + stockId);
        } else {
            System.out.println("Stock not found in DB for ID: " + stockId);
        }
    }
}
