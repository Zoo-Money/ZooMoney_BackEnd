package com.shinhan.zoomoney.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

	MemberEntity findByMemberIdAndMemberPw(String member_id,String member_pw);
	List<MemberEntity> findByMemberNum(int memberNum);
}