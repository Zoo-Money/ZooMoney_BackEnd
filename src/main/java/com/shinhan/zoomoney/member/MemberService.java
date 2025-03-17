package com.shinhan.zoomoney.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

	
    @Autowired
    private MemberRepository memberRepository;

    public MemberEntity login(String member_id, String member_pw) {
    	 return memberRepository.findByMemberIdAndMemberPw(member_id, member_pw);
    }
    public List<MemberEntity> selectByMemberNum(int member_num) {
        return memberRepository.findByMemberNum(member_num);
    }
    public String getMemberAccount(String memberId) {
    	   return memberRepository.findByMemberId(memberId)
                   .map(MemberEntity::getMemberAccount)
                   .orElse("계좌 정보가 없습니다.");
       }
}
