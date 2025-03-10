package com.shinhan.zoomoney.notify;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyRepository extends JpaRepository<NotifyEntity, Integer> {

    // 멤버별 알림 목록 조회
    List<NotifyEntity> findAllByMember_MemberNum(int memberNum);

    // 읽지 않은 알림 개수 조회
    @Query("SELECT COUNT(n) FROM NotifyEntity n WHERE n.member.memberNum = :memberNum AND n.notifyCheck = false")
    int countByUnread(int memberNum);
}
