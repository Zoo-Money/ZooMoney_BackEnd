package com.shinhan.zoomoney.card.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shinhan.zoomoney.card.entity.UseHistoryEntity;

public interface UseHistoryRepositiory extends JpaRepository<UseHistoryEntity, Integer> {
        @Query("SELECT u FROM UseHistoryEntity u " +
                        "JOIN CardEntity c ON u.card.cardNum = c.cardNum " +
                        "JOIN MemberEntity m ON c.member.memberNum = m.memberNum " +
                        "WHERE m.memberNum = :memberNum ORDER BY u.usehistDate DESC")
        List<UseHistoryEntity> findByMember(@Param("memberNum") Integer memberNum);

        @Query("SELECT u FROM UseHistoryEntity u " +
                        "JOIN CardEntity c ON u.card.cardNum = c.cardNum " +
                        "JOIN MemberEntity m ON c.member.memberNum = m.memberNum " +
                        "WHERE m.memberNum = :memberNum and u.usehistDate >= :startDate ORDER BY u.usehistDate DESC")
        List<UseHistoryEntity> findByMemberAndPeriod(@Param("memberNum") Integer memberNum,
                        @Param("startDate") LocalDateTime startDate);

}
