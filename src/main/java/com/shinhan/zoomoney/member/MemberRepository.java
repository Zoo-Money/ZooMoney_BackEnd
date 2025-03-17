package com.shinhan.zoomoney.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

	//로그인	
	MemberEntity findByMemberIdAndMemberPw(String member_id,String member_pw);
	//등록된 카드 가져오기
	List<MemberEntity> findByMemberNum(int memberNum);
	 
    // 부모 ID 조회 
    Optional<MemberEntity> findByMemberId(String memberId);
    // 부모의 아이들 조회
    List<MemberEntity> findByMemberParent(MemberEntity memberParent);
    
 
}