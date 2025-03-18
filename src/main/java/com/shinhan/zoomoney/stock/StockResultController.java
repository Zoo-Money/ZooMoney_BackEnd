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
public class StockResultController {

	@Autowired
	StockResultService resultService;
	
	@GetMapping("/list")
	public List<StockResultDto> selectAllResult(HttpSession session){
		Integer memberNum = (Integer) session.getAttribute("memberNum");
		if (memberNum == null) {
	        return new ArrayList<>(); // 빈 리스트 반환
	    }
		return resultService.selectAllByMemberNum(memberNum);
	}
}
