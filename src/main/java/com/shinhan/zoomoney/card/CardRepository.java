package com.shinhan.zoomoney.card;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<CardEntity, String> {
    
	 // 네이티브 SQL을 사용하여 member_num 기준으로 카드 조회
    @Query(value = "SELECT * FROM card WHERE member_num = :memberNum", nativeQuery = true)
    List<CardEntity> findByMemberNum(@Param("memberNum") Integer memberNum);
    
}
