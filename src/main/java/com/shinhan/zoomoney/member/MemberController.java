package com.shinhan.zoomoney.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam("member_id") String member_id,
            @RequestParam("member_pw") String member_pw, HttpSession session) {
        MemberEntity member = memberService.login(member_id, member_pw);
        Map<String, Object> response = new HashMap<>();
        if (member != null) {
            // 세션에 값 저장
            session.setAttribute("member_id", member.getMemberId());
            session.setAttribute("member_num", member.getMemberNum());
            session.setAttribute("member_phone", member.getMemberPhone());
            session.setAttribute("member_point", member.getMemberPoint());
            session.setAttribute("member_name", member.getMemberName());
            session.setAttribute("member_type", member.getMemberType());
            session.setAttribute("member_parent", member.getMemberParent());
            // 로그인 성공 메시지와 함께 세션 정보를 응답으로 반환
            response.put("message", "로그인 성공");
            response.put("member_id", member.getMemberId());
            response.put("member_num", member.getMemberNum());
            response.put("member_phone", member.getMemberPhone());
            response.put("member_point", member.getMemberPoint());
            response.put("member_name", member.getMemberName());
            response.put("member_type", member.getMemberType());
            response.put("member_parent", member.getMemberParent());
        } else {
            response.put("message", "로그인 실패");
        }
        return response;
    }

    @GetMapping("/logout")
    public Map<String, String> logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        Map<String, String> response = new HashMap<>();
        response.put("message", "로그아웃 성공");
        return response;
    }

    @GetMapping("/select")
    public List<MemberEntity> select(@RequestParam("memberNum") int memberNum) {
        List<MemberEntity> memberInfo = memberService.selectByMemberNum(memberNum);
        return memberInfo;
    }

    // 포인트 조회 api
    @GetMapping("/point/{memberNum}")
    public Map<String, Integer> getMemberPoint(@PathVariable("memberNum") int memberNum) {
        Integer point = memberService.getMemberPoint(memberNum);
        Map<String, Integer> response = new HashMap<>();
        response.put("member_point", point);
        return response;
    }

}