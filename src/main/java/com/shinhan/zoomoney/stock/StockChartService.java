package com.shinhan.zoomoney.stock;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;

@Service
public class StockChartService {
	private final RestTemplate restTemplate = new RestTemplate();
	private final StockChartTokenService tokenService;
	private final StockChartRepository stockRepository;
	private StockChartRepository stockChartRepository;
	
	@Value("${api.key}")
	private String apiKey;
	
	@Value("${api.secret}")
	private String apiSecret;
	
	
	// API URL (시가총액 기준 TOP 30개만 가져올 수 있음)
    private static final String apiUrl = "https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/ranking/market-cap";
    
    public StockChartService( StockChartTokenService tokenService, StockChartRepository stockRepository) {
    	this.tokenService = tokenService;
    	this.stockRepository = stockRepository;
    }
    
    public List<Map<String, Object>> getTopStocks() {
        try {
        	String accessToken = tokenService.getAccessToken();
        	
        	System.out.println("api 읽기 완료" + accessToken);
        	
            // HTTP 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json; charset=utf-8");
            headers.set("authorization","Bearer " + accessToken);
            headers.set("appkey", apiKey);
            headers.set("appsecret", apiSecret);
            headers.set("tr_id", "FHPST01740000");
            headers.set("custtype","P");
            
            // GET 요청에 사용할 query parameters 설정
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("fid_cond_mrkt_div_code", "J")
                    .queryParam("fid_cond_scr_div_code", "20174")
                    .queryParam("fid_div_cls_code", "0")
                    .queryParam("fid_input_iscd", "0000")
                    .queryParam("fid_trgt_cls_code", "0")
                    .queryParam("fid_trgt_exls_cls_code", "0")
                    .queryParam("fid_input_price_1", "")
                    .queryParam("fid_input_price_2", "")
                    .queryParam("fid_vol_cnt", "");
            
            String finalUrl = uriBuilder.toUriString();
            System.out.println("headers,requestBody 만들기 ");
            
            //Get 요청 보내기
            HttpEntity<Object> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(finalUrl, HttpMethod.GET, entity, Map.class);
            
            // 응답 데이터 파싱
            Map<String, Object> responseBody = response.getBody();
            System.out.println("API 응답 데이터: " + responseBody);
            
            // JSON 구조에 따라서 데이터 가져오기
            List<Map<String, Object>> stockList = null;
            if (responseBody != null && responseBody.containsKey("output")) {
                stockList = (List<Map<String, Object>>) responseBody.get("output");
            } else {
                System.out.println("예상한 데이터 형식이 아닙니다. 응답을 확인해주세요.");
            }

            System.out.println("주식 리스트 개수: " + (stockList != null ? stockList.size() : 0));
            return stockList;
            
            
            

        } catch (Exception e) {
            e.printStackTrace();
            return List.of(Map.of("error", "Failed to fetch market cap data"));
        }
        
        
    }
    // 주식 리스트를 저장하는 메서드
    @Transactional
    public void saveStockList(List<StockDto> stockDtoList) {
        List<StockEntity> stockEntities = stockDtoList.stream()
                .map(StockDto::toEntity) // 변환
                .collect(Collectors.toList());

        stockRepository.saveAll(stockEntities); // DB에 저장
    }
    
    
}
