package com.shinhan.zoomoney.notify;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyRepository extends JpaRepository<NotifyEntity, Integer> {

    // 사용자의 알림 목록 조회
    @Query("SELECT e FROM NotifyEntity e WHERE e.member.memberNum = :memberNum ORDER BY e.notifyTime DESC")
    List<NotifyEntity> findAllByMember_MemberNum(@Param("memberNum") int memberNum);

    // 읽지 않은 알림 개수 조회
    @Query("SELECT COUNT(e) FROM NotifyEntity e WHERE e.member.memberNum = :memberNum AND e.notifyCheck = false")
    int countByUnread(@Param("memberNum") int memberNum);
}
