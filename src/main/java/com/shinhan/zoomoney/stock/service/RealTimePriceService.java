package com.shinhan.zoomoney.stock.service;

import java.net.URI;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                                            "content-type", "utf-8"),
                                    "body", Map.of(
                                            "input", Map.of(
                                                    "tr_id", "H0STCNT0",
                                                    "tr_key", tr_key))));

                            // 메시지 전송
                            return session.send(Mono.just(session.textMessage(jsonMessage)))
                                    .thenMany(session.receive()
                                            .map(WebSocketMessage::getPayloadAsText)
                                            .doOnNext(response -> {
                                                sink.next(response);
                                            })
                                            .doOnError(sink::error)
                                            .doOnComplete(sink::complete))
                                    .then();
                        } catch (JsonProcessingException e) {
                            sink.error(e);
                            return Mono.empty(); // Mono<Void> 반환
                        }
                    }).subscribe(); // WebSocket 연결 실행
        });
    }
}