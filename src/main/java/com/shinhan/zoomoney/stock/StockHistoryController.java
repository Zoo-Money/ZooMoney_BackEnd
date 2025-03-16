package com.shinhan.zoomoney.stock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/stock/result")
public class StockHistoryController {

	@Autowired
	StockHistoryService historyService;
	
	@GetMapping("/list")
	public List<StockResultDto> selectAllResult(HttpSession session){
		//테스트용 세션 추가 - 추후 지우기
		session.setAttribute("memberNum", 1);
		Integer memberNum = (Integer) session.getAttribute("memberNum");
		if (memberNum == null) {
	        // 세션에 memberNum이 없다면 에러 처리
	        return new ArrayList<>(); // 빈 리스트 반환 (또는 적절한 응답)
	    }
		return historyService.selectAllByMemberNum(memberNum);
	}
}
