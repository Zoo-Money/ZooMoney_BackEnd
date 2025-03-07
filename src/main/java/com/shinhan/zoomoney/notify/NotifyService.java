package com.shinhan.zoomoney.notify;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyService {
    @Autowired
    private NotifyRepository notifyRepository;

    // 알림 조회
    public List<NotifyDto> select(int member_num) {
        return notifyRepository.select(member_num);
    }

    // 알림 상세 조회
    public NotifyDto selectById(int notify_num) {
        return notifyRepository.selectById(notify_num);
    }

    // 읽지 않은 알림 개수 조회
    public int selectUnread(int member_num) {
        return notifyRepository.selectUnread(member_num);
    }

    // 알림 생성
    public void insert(NotifyDto nofityDTO) {
        notifyRepository.insert(nofityDTO);
    }

    // 알림 상태 변경
    public void update(int notify_num) {
        notifyRepository.update(notify_num);
    }
}