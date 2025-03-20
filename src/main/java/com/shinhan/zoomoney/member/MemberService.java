package com.shinhan.zoomoney.member;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // 카드 이미지 변경시 포인트 차감
    public void deductMemberPoint(Integer memberNum) {
        // memberNum이 null인 경우 예외 처리
        if (memberNum == null) {
            throw new IllegalArgumentException("memberNum이 null입니다.");
        }

        Optional<MemberEntity> optionalMember = memberRepository.findById(memberNum);
        int minusPoint = 10000;

        if (optionalMember.isPresent()) {
            MemberEntity member = optionalMember.get();

            // 현재 포인트 확인 (null이면 0으로 설정)
            Integer currentPoint = member.getMemberPoint() != null ? member.getMemberPoint() : 0;

            if (currentPoint < minusPoint) {
                throw new IllegalArgumentException("포인트가 부족합니다.");
            }

            // 포인트 차감 후 저장
            member.setMemberPoint(currentPoint - minusPoint);
            memberRepository.save(member);

        } else {
            throw new IllegalArgumentException("해당 회원을 찾을 수 없습니다.");
        }
    }

    public String getMemberAccount(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .map(MemberEntity::getMemberAccount)
                .orElse("계좌 정보가 없습니다.");
    }

    // 포인트 조회
    public Integer getMemberPoint(int memberNum) {
        return memberRepository.findMemberPointByMemberNum(memberNum);
    }

}
