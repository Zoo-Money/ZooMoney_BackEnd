//package com.shinhan.zoomoney.notify;
//
//import java.util.List;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface NotifyRepository extends JpaRepository<NotifyEntity, Integer> {
//
//    // 알림 조회
//    @Query("SELECT n FROM NotifyEntity n WHERE n.member_num = :member_num")
//    List<NotifyEntity> findAllByMemberNum(int member_num);
//
//    // 읽지 않은 알림 개수 조회
//    @Query("SELECT COUNT(n) FROM NotifyEntity n WHERE n.member_num = :member_num AND n.read = false")
//    int countUnreadByMemberNum(int member_num);
//}
