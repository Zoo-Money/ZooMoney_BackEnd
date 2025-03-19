package com.shinhan.zoomoney.stock;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    @Value("${stock.api.key}")
    private String app_key;

    @Value("${stock.api.secret}")
    private String secret_key;

    String approvalKey;

    @Autowired
    RealTimePriceService service;

    @Autowired
    ApprovalKeyService approvalKeyService;

    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionKey = session.getId();
        sessions.put(sessionKey, session);
        // 주기적으로 데이터 전송 (1초마다)
        executor.scheduleAtFixedRate(() -> {
            // 세션의 tr_key(종목코드)를 기반으로 API 요청
            String trKey = (String) session.getAttributes().get("tr_key");
            if (trKey != null) {
                Flux<String> stockData = fetchStockData(trKey);
                stockData.subscribe(this::sendToAll);
            } else {
                System.err.println("tr_key가 세션에 없습니다.");
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> messageData = objectMapper.readValue(payload, Map.class);
        String trKey = messageData.get("symbol");

        if (trKey != null) {
            session.getAttributes().put("tr_key", trKey);
        } else {
            System.err.println("tr_key가 메시지에 없습니다.");
        }
    }

    private Flux<String> fetchStockData(String stockCode) {
        if (sessions.size() == 1) {
            approvalKey = approvalKeyService.getApprovalKeySync(app_key, secret_key);
        }
        return service.subscribeRealTimePrice(approvalKey, stockCode);
    }

    private void sendToAll(String message) {
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                try {
                    synchronized (session) {
                        session.sendMessage(new TextMessage(message));
                    }
                } catch (IOException | IllegalStateException e) {
                    System.err.println("메시지 전송 중 오류 발생: " + e.getMessage());
                }
            }
        }
    }
}