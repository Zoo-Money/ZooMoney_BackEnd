package com.shinhan.zoomoney.stock;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.zoomoney.member.MemberEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/stock/history")
public class StockHistoryController {

	@Autowired
	StockHistoryService historyService;
	
	@GetMapping("/list")
	public List<StockHistoryDto> selectAllHistory( @RequestParam("memberNum") Integer memberNum,  HttpServletRequest request){
		//Integer memberNum = 1;
//		HttpSession session  = request.getSession();
//		System.out.println(session.isNew());
//		MemberEntity member = (MemberEntity) session.getAttribute("member2");
//		System.out.println(member);
//		if (member == null) {
//	        return new ArrayList<>(); // 빈 리스트 반환
//	    }
		List<StockHistoryDto> result = historyService.selectStockHitory(memberNum);
		System.out.println(result);
		return result;
	}

}
