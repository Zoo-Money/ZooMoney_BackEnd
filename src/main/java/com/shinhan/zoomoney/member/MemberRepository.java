package com.shinhan.zoomoney.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

	//로그인	
	MemberEntity findByMemberIdAndMemberPw(String member_id,String member_pw);
	//등록된 카드 가져오기
	List<MemberEntity> findByMemberNum(int memberNum);
}