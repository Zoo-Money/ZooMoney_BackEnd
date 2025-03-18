package com.shinhan.zoomoney.stock;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@Service
public class RealTimePriceService {
    private final WebSocketClient webSocketClient = new ReactorNettyWebSocketClient();

    private static final String WEBSOCKET_URL = "ws://ops.koreainvestment.com:21000/tryitout/H0STCNT0"; // 웹소켓 서버 URL
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Flux<String> subscribeRealTimePrice(String approvalKey, String tr_key) {
        return Flux.create(sink -> {
            webSocketClient.execute(
                    URI.create(WEBSOCKET_URL),
                    session -> {
                        try {
                            // JSON 메시지 생성
                            String jsonMessage = objectMapper.writeValueAsString(Map.of(
                                    "header", Map.of(
                                            "approval_key", approvalKey,
                                            "custtype", "P",
                                            "tr_type", "1",
                                            "content-type", "utf-8"
                                    ),
                                    "body", Map.of(
                                            "input", Map.of(
                                                    "tr_id", "H0STCNT0",
                                                    "tr_key", tr_key
                                            )
                                    )
                            ));

                            System.out.println("Request JSON: " + jsonMessage);

                            // 메시지 전송
                            return session.send(Mono.just(session.textMessage(jsonMessage)))
                                    .thenMany(session.receive()
                                            .map(WebSocketMessage::getPayloadAsText)
                                            .doOnNext(response -> {
                                                System.out.println("Received22: " + response);
                                                sink.next(response); // Flux<String>에 데이터 전송
                                            })
                                            .doOnError(sink::error) // 에러 발생 시 sink.error 호출
                                            .doOnComplete(sink::complete) // 완료 시 sink.complete 호출
                                    )
                                    .then();
                        } catch (JsonProcessingException e) {
                            sink.error(e); // JSON 변환 중 에러 발생 시 Flux 에러 전송
                            return Mono.empty(); // Mono<Void> 반환 (필수)
                        }
                    }
            ).subscribe(); // WebSocket 연결 실행
        });
    }
    public Mono<Void> unsubscribeRealTimePrice(String approvalKey, String stockCode) {
        if (approvalKey == null || approvalKey.isEmpty() || stockCode == null || stockCode.isEmpty()) {
            System.out.println("[ERROR] 구독 해제 실패: approvalKey 또는 종목 코드가 없음");
            return Mono.error(new IllegalArgumentException("approvalKey 또는 종목 코드가 없습니다"));
        }

        System.out.println("[DEBUG] unsubscribeRealTimePrice() 호출됨 - approvalKey: " + approvalKey + ", 종목코드: " + stockCode);

        try {
            String jsonMessage = objectMapper.writeValueAsString(Map.of(
                    "header", Map.of(
                            "approval_key", approvalKey,
                            "custtype", "P",
                            "tr_type", "2",  // 구독 해제
                            "content-type", "utf-8"
                    ),
                    "body", Map.of(
                            "input", Map.of(
                                    "tr_id", "H0STCNT0",
                                    "tr_key", stockCode  // ✅ stockCode가 올바르게 들어가도록 수정
                            )
                    )
            ));

            System.out.println("[DEBUG] Unsubscribe Request JSON: " + jsonMessage);

            // WebSocket 클라이언트를 사용하여 구독 해제 메시지 전송 및 응답 처리
            return webSocketClient.execute(
                    URI.create(WEBSOCKET_URL),
                    session -> session.send(Mono.just(session.textMessage(jsonMessage)))  // 메시지 전송
                            .thenMany(session.receive()  // 응답을 받고 처리
                                    .map(WebSocketMessage::getPayloadAsText)
                                    .doOnNext(response -> System.out.println("[DEBUG] Unsubscribe Response: " + response))
                            )
                            .then()  // 작업 완료 후
            ).then();
        } catch (JsonProcessingException e) {
            System.out.println("[ERROR] JSON 변환 오류: " + e.getMessage());
            return Mono.error(e);  // JSON 변환 오류 시 에러 반환
        }
    }

}