package com.shinhan.zoomoney.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

	MemberEntity findByMemberIdAndMemberPw(String member_id,String member_pw);
	List<MemberEntity> findByMemberNum(int memberNum);
	 
    // 부모 ID 조회 메서드 추가
    Optional<MemberEntity> findByMemberId(String memberId);
}