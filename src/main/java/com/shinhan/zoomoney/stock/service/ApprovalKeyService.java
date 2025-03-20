package com.shinhan.zoomoney.stock.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class ApprovalKeyService {

    private final WebClient webClient;
    private String approvalKey;

    public ApprovalKeyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://openapi.koreainvestment.com:9443").build();
    }

    public String getApprovalKeySync(String app_key, String secret_key) {
        getApprovalKey(app_key, secret_key).block();
        return approvalKey;
    }

    public Mono<String> getApprovalKey(String app_key, String secret_key) {
        return webClient.post()
                .uri("/oauth2/Approval")
                .header("Content-Type", "application/json")
                .bodyValue("""
                            {
                                "grant_type": "client_credentials",
                                "appkey": "%s",
                                "secretkey": "%s"
                            }
                        """.formatted(app_key, secret_key))
                .retrieve()
                .bodyToMono(String.class) // JSON 응답을 문자열로 반환
                .doOnSuccess(response -> {
                    // 응답에서 approval_key 값을 파싱하여 저장
                    approvalKey = parseApprovalKey(response);
                });
    }

    // JSON 응답에서 approval_key 값을 파싱
    private String parseApprovalKey(String response) {
        try {
            // ObjectMapper를 사용하여 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            return rootNode.path("approval_key").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
