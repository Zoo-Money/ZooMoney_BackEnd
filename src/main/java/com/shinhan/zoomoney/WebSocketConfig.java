package com.shinhan.zoomoney;

import com.shinhan.zoomoney.stock.MyWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
	private final MyWebSocketHandler myWebSocketHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // WebSocket 요청을 처리할 핸들러 등록
        registry.addHandler(myWebSocketHandler, "/ws/stocks").setAllowedOrigins("*");
    }

    
}