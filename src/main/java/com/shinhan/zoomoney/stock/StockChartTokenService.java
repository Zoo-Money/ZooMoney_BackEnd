package com.shinhan.zoomoney.stock;

import java.io.Console;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
public class StockChartTokenService {
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Value("${api.key}")
    private String appKey;

    @Value("${api.secret}")
    private String appSecret;
    
    @PostConstruct
    public void init() {
        System.out.println("✅ StockChartTokenService 초기화됨");
        System.out.println("🔑 API Key: " + appKey);
        System.out.println("🔐 API Secret: " + appSecret);
    }
	
    private static final String tokenUrl = "https://openapi.koreainvestment.com:9443/oauth2/tokenP";
    
    private String accessToken = null;
    private long tokenExpiration = 0;
    
    public String getAccessToken() {
    	long currentTime = System.currentTimeMillis();
        
        // 기존 토큰이 유효하면 그대로 사용
        if (accessToken != null && currentTime < tokenExpiration) {
            return accessToken;
        }
        
        
        // 새로운 토큰 발급 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // 요청 키값 매핑
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grant_type", "client_credentials");
        requestBody.put("appkey", appKey);
        requestBody.put("appsecret", appSecret);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, Map.class);
        
        // 토큰 만료 시간 예상
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("access_token")) {
            accessToken = (String) responseBody.get("access_token");
            long expiresIn = ((Number) responseBody.get("expires_in")).longValue() * 1000; // 초 → 밀리초 변환
            tokenExpiration = currentTime + expiresIn;

            return accessToken;
        }
        throw new RuntimeException("Failed to fetch access token");
    }
    
    
}
