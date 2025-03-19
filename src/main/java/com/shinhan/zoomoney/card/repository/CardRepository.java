package com.shinhan.zoomoney.card.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shinhan.zoomoney.card.entity.CardEntity;
import com.shinhan.zoomoney.member.MemberEntity;

public interface CardRepository extends JpaRepository<CardEntity, String> {

    CardEntity findByMember(MemberEntity member);

    CardEntity findByMemberMemberNumAndCardNum(Integer memberNum, String cardNum);
}
