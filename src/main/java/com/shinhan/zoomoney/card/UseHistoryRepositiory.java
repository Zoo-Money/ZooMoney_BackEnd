package com.shinhan.zoomoney.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UseHistoryRepositiory extends JpaRepository<UseHistoryEntity, Integer>  {
    @Query("SELECT u FROM UseHistoryEntity u " +
            "JOIN CardEntity c ON u.card.cardNum = c.cardNum " +
            "JOIN MemberEntity m ON c.member.memberNum = m.memberNum " +
            "WHERE m.memberNum = :memberNum")
    List<UseHistoryEntity> findByMember(@Param("memberNum") Integer memberNum);

    @Query("SELECT u FROM UseHistoryEntity u " +
            "JOIN CardEntity c ON u.card.cardNum = c.cardNum " +
            "JOIN MemberEntity m ON c.member.memberNum = m.memberNum " +
            "WHERE m.memberNum = :memberNum and u.usehistDate >= :startDate")
    List<UseHistoryEntity> findByMemberAndPeriod(@Param("memberNum") Integer memberNum, @Param("startDate") LocalDateTime startDate);

}
