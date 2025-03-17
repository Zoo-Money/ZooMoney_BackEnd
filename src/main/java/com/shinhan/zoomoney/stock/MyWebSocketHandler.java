package com.shinhan.zoomoney.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, String> sessionStockMap = new ConcurrentHashMap<>(); // 세션별 종목 코드 저장
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
        System.out.println("WebSocket 연결됨: " + sessionKey);
        sessions.put(sessionKey, session);
        System.out.println(sessions);
        // 주기적으로 데이터 전송 (1초마다)
        executor.scheduleAtFixedRate(() -> {
            // 세션의 tr_key를 기반으로 API 요청
            String trKey = (String) session.getAttributes().get("tr_key");
            if (trKey != null) {
                Flux<String> stockData = fetchStockData(trKey); // tr_key 값으로 실시간 주식 데이터 요청
                stockData.subscribe(this::sendToAll);
            } else {
                System.err.println("tr_key가 세션에 없습니다.");
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
        System.out.println("WebSocket 연결 종료: " + session.getId());
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 메시지에서 tr_key를 추출
        String payload = message.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> messageData = objectMapper.readValue(payload, Map.class);
        String trKey = messageData.get("symbol");

        if (trKey != null) {
            session.getAttributes().put("tr_key", trKey);  // 세션에 tr_key 저장
            System.out.println("tr_key received: " + trKey);
        } else {
            System.err.println("tr_key가 메시지에 없습니다.");
        }
    }

    private Flux<String> fetchStockData(String stockCode) {
        if (sessions.size()==1) {
            approvalKey = approvalKeyService.getApprovalKeySync(app_key, secret_key);
        }
        System.out.println("approvalKey : "+approvalKey);
        return service.subscribeRealTimePrice(approvalKey, stockCode);
    }

    private void sendToAll(String message) {
        System.out.println("접속된 session 수 : "+sessions.size());
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                try {
                    synchronized (session) {  // :작은_파란색_다이아몬드: 동기화하여 하나의 메시지씩 전송
                        session.sendMessage(new TextMessage(message));
                    }
                } catch (IOException | IllegalStateException e) {
                    System.err.println("메시지 전송 중 오류 발생: " + e.getMessage());
                }
            }
        }
    }
}