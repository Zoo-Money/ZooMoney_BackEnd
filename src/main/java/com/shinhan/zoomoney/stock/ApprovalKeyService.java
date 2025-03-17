package com.shinhan.zoomoney.stock;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ApprovalKeyService {

    private final WebClient webClient;
    private String approvalKey;

    public ApprovalKeyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://openapi.koreainvestment.com:9443").build();
    }

    // approvalKey를 동기적으로 가져오는 방법
    public String getApprovalKeySync(String app_key, String secret_key) {
        // getApprovalKey 호출 후 block()으로 결과를 동기적으로 기다림
        getApprovalKey(app_key, secret_key).block(); // 비동기 메서드를 동기화
        System.out.println("approvalKey: "+approvalKey);
        return approvalKey;  // approvalKey가 갱신된 후 반환
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
                .bodyToMono(String.class)  // JSON 응답을 문자열로 반환
                .doOnSuccess(response -> {
                    // 응답에서 approval_key 값을 파싱하여 저장
                    approvalKey = parseApprovalKey(response);
                });
    }
    // JSON 응답에서 approval_key 값을 파싱하는 메서드
    private String parseApprovalKey(String response) {
        try {
            // ObjectMapper를 사용하여 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            // approval_key 값 추출
            return rootNode.path("approval_key").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 에러 발생 시 null 반환
        }
    }
}
