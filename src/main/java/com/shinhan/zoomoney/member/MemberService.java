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
}
