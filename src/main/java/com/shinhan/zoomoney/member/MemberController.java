package com.shinhan.zoomoney.member;

import java.util.HashMap;
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
    public Map<String, String> login(@RequestParam("member_id") String member_id, @RequestParam ("member_pw")String member_pw, HttpSession session) {
        MemberEntity member = memberService.login(member_id, member_pw);
        Map<String, String> response = new HashMap<>();

        if (member != null) {
            session.setAttribute("member_id", member.getMemberId());  
            session.setAttribute("member_num", member.getMemberNum()); 
            session.setAttribute("member_phone", member.getMemberPhone());
            session.setAttribute("member_point", member.getMemberPoint()); 
            session.setAttribute("member_name", member.getMemberName());
            session.setAttribute("member_type", member.getMemberName());
            session.setAttribute("member_parent", member.getMemberName());


            
            response.put("message", "로그인 성공");
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
	
	
}