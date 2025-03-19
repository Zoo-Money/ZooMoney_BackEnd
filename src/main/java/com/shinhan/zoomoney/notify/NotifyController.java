package com.shinhan.zoomoney.notify;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notify")
public class NotifyController {
    @Autowired
    private NotifyService notifyService;

    // Thread-Safe한 SSE 연결을 관리하는 자료구조
    private Map<Integer, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    private static final Long TIMEOUT = 30 * 60 * 1000L; // 30분 유지

    // SSE 연결 설정 (클라이언트가 알림을 구독)
    @GetMapping(value = "/subscribe/{memberNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable("memberNum") int memberNum) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitterMap.put(memberNum, emitter);

        // 클라이언트에 연결 확인 이벤트 전송
        try {
            emitter.send(SseEmitter.event().name("INIT").data(""));
        } catch (IOException e) {
            emitterMap.remove(memberNum);
        }

        // 연결 종료 시, 맵에서 해당 사용자 제거
        emitter.onCompletion(() -> emitterMap.remove(memberNum));
        emitter.onTimeout(() -> emitterMap.remove(memberNum));

        return emitter;
    }

    // 새로운 알림 생성 및 SSE로 전송
    @PostMapping("/send")
    public void sendNotification(@RequestBody NotifyDto dto) {
        // 알림 생성
        notifyService.insert(dto);

        // SSE 연결이 존재할 경우 알림 전송
        SseEmitter emitter = emitterMap.get(dto.getMemberNum());
        if (emitter != null) {
            try {
                String data = URLEncoder.encode(dto.getNotifyContent(), StandardCharsets.UTF_8.toString());

                emitter.send(SseEmitter.event().name("NOTIFY").data(data));
            } catch (IOException e) {
                emitterMap.remove(dto.getMemberNum());
            }
        }
    }

    // 사용자의 알림 목록 조회
    @GetMapping("/list/{memberNum}")
    public List<NotifyDto> select(@PathVariable("memberNum") int memberNum) {
        return notifyService.select(memberNum);
    }

    // 알림 상세 조회
    @PostMapping("/select/{notifyNum}")
    public NotifyDto selectById(@PathVariable("notifyNum") int notifyNum) {
        return notifyService.entityToDto(notifyService.selectById(notifyNum));
    }

    // 읽지 않은 알림 개수 조회
    @GetMapping("/unread/{memberNum}")
    public int selectUnread(@PathVariable("memberNum") int memberNum) {
        return notifyService.selectUnread(memberNum);
    }

    // 알림 상태(읽음 여부) 변경
    @PutMapping("/check/{notifyNum}")
    public void update(@PathVariable("notifyNum") int notifyNum) {
        notifyService.update(notifyNum);
    }
}