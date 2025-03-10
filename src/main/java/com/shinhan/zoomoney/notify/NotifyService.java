package com.shinhan.zoomoney.notify;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyService {

    @Autowired
    private NotifyRepository notifyRepository;

    @Autowired
    private ModelMapper modelMapper;

    // 알림 조회
    public List<NotifyDto> select(int member_num) {
        List<NotifyEntity> entityList = notifyRepository.findAllByMember_MemberNum(member_num);
        List<NotifyDto> dtoList = entityList.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return dtoList;
    }

    // 알림 상세 조회
    public NotifyEntity selectById(int notify_num) {
        return notifyRepository.findById(notify_num).orElse(null);
    }

    // 읽지 않은 알림 개수 조회
    public int selectUnread(int member_num) {
        return notifyRepository.countByUnread(member_num);
    }

    // 알림 생성
    public void insert(NotifyDto notifyDTO) {
        NotifyEntity entity = dtoToEntity(notifyDTO);
        notifyRepository.save(entity);
    }

    // 알림 상태 변경
    public void update(int notify_num) {
        NotifyEntity notifyEntity = notifyRepository.findById(notify_num).orElse(null);
        if (notifyEntity != null) {
            // 상태 변경 로직
            notifyEntity.setNotifyCheck(true);  // 읽음으로 상태 변경
            notifyRepository.save(notifyEntity);
        }
    }

    public NotifyEntity dtoToEntity(NotifyDto dto) {
        return modelMapper.map(dto, NotifyEntity.class);
    }

    public NotifyDto entityToDto(NotifyEntity entity) {
        if (entity == null) return null;
        
        NotifyDto dto = new NotifyDto();
        dto.setNotify_num(entity.getNotifyNum());
        dto.setMember_num(entity.getMember().getMemberNum());
        dto.setNotify_content(entity.getNotifyContent());
        dto.setNotify_url(entity.getNotifyUrl());
        dto.setNotify_time(entity.getNotifyTime());
        dto.setNotify_check(entity.isNotifyCheck());
        
        return dto;
    }
}
