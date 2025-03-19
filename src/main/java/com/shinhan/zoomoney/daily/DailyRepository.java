package com.shinhan.zoomoney.daily;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shinhan.zoomoney.member.MemberEntity;

public interface DailyRepository extends JpaRepository<DailyEntity, Integer> {

    // 해당 날짜의 출석체크 여부 확인
    boolean existsByMemberAndDailyDate(MemberEntity member, Date dailyDate);

}
