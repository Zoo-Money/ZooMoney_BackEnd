package com.shinhan.zoomoney.notify;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyService {

    @Autowired
    private NotifyRepository notifyRepo;

    @Autowired
    private ModelMapper modelMapper;

    // 사용자의 알림 목록 조회
    public List<NotifyDto> select(int memberNum) {
        List<NotifyEntity> entityList = notifyRepo.findAllByMember_MemberNum(memberNum);
        List<NotifyDto> dtoList = entityList.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return dtoList;
    }

    // 알림 상세 조회
    public NotifyEntity selectById(int notifyNum) {
        return notifyRepo.findById(notifyNum).orElse(null);
    }

    // 읽지 않은 알림 개수 조회
    public int selectUnread(int memberNum) {
        return notifyRepo.countByUnread(memberNum);
    }

    // 알림 생성
    public void insert(NotifyDto dto) {
        NotifyEntity entity = dtoToEntity(dto);
        notifyRepo.save(entity);
    }

    // 알림 상태(읽음 여부) 변경
    public void update(int notifyNum) {
        NotifyEntity entity = notifyRepo.findById(notifyNum).orElse(null);

        if (entity != null) {
            entity.setNotifyCheck(true); // 읽음으로 상태 변경
            notifyRepo.save(entity);
        }
    }

    // DTO → Entity 변환
    public NotifyEntity dtoToEntity(NotifyDto dto) {
        return modelMapper.map(dto, NotifyEntity.class);
    }

    // Entity → DTO 변환
    public NotifyDto entityToDto(NotifyEntity entity) {
        return modelMapper.map(entity, NotifyDto.class);
    }
}
