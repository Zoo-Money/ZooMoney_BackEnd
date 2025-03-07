package com.shinhan.zoomoney.notify;

import java.io.IOException;
import java.net.URLEncoder;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notify")
public class NotifyController {
    @Autowired
    private NotifyService notifyService;
    private Map<Integer, SseEmitter> map = new ConcurrentHashMap<>(); // Thread-Safe 자료구조

    private static final Long TIMEOUT = 30 * 60 * 1000L; // 30분

    @GetMapping("/main")
    public ModelAndView viewMain() {
        return new ModelAndView("notify/main");
    }

    // SSE 연결 설정
    @GetMapping(value = "/subscribe/{member_num}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable int user_id) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        map.put(user_id, emitter);

        // 클라이언트에 연결 확인 이벤트 전송
        try {
            emitter.send(SseEmitter.event().name("INIT").data(""));
        } catch (IOException e) {
            map.remove(user_id);
        }

        emitter.onCompletion(() -> map.remove(user_id));
        emitter.onTimeout(() -> map.remove(user_id));

        return emitter;
    }

    // 새로운 알림 생성 및 전송
    @PostMapping("/send")
    public void sendNotification(@RequestBody NotifyDto notifyDTO) {
        // 알림 생성
        notifyService.insert(notifyDTO);

        // SSE 연결이 존재할 경우 알림 전송
        SseEmitter emitter = map.get(notifyDTO.getMember_num());
        if (emitter != null) {
            try {
                String data = URLEncoder.encode(notifyDTO.getNotify_content(), "UTF-8");

                emitter.send(SseEmitter.event().name("NOTIFY").data(data));
            } catch (IOException e) {
                map.remove(notifyDTO.getMember_num());
            }
        }
    }

    // 사용자의 알림 목록 조회
    @GetMapping("/list/{member_num}")
    public List<NotifyDto> select(@PathVariable int member_num) {
        return notifyService.select(member_num);
    }

    // 알림 상세 조회
    @PostMapping("/select/{notify_num}")
    public NotifyDto selectById(@PathVariable int notify_num) {
        return notifyService.selectById(notify_num);
    }

    // 읽지 않은 알림 개수 조회
    @GetMapping("/unread/{member_num}")
    public int selectUnread(@PathVariable int member_num) {
        return notifyService.selectUnread(member_num);
    }

    // 알림 상태 변경
    @PutMapping("/check/{notify_num}")
    public void update(@PathVariable int notify_num) {
        notifyService.update(notify_num);
    }
}