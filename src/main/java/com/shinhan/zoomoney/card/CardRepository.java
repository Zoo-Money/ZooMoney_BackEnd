package com.shinhan.zoomoney.card;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shinhan.zoomoney.member.MemberEntity;

public interface CardRepository extends JpaRepository<CardEntity, String> {
    
	
    CardEntity findByMember(MemberEntity member);
  
    
}
