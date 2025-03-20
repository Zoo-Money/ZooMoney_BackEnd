package com.shinhan.zoomoney.moneyplan.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.zoomoney.moneyplan.dto.MoneyPlanDto;
import com.shinhan.zoomoney.moneyplan.service.MoneyPlanService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/moneyplan")
public class MoneyPlanController {

	@Autowired
	MoneyPlanService moneyPlanService;

	@GetMapping("/getAllowance")
	public ResponseEntity<?> getAllowance(@RequestParam("memberNum") int memberNum) {
		int allowance = moneyPlanService.getAllowance(memberNum);
		return ResponseEntity.ok(allowance);
	}

	@PostMapping("/save/{memberNum}")
	public ResponseEntity<String> saveMoneyPlan(
			@RequestBody Map<String, Object> requestData, // planMoney와 categoryAmounts를 받는 Map
			@PathVariable("memberNum") int memberNum) {
		int planMoney = (Integer) requestData.get("planMoney"); // planMoney 추출

		// categoryAmounts는 Map<String, String>으로 받아서 Map<Integer, Integer>로 변환
		Map<String, Object> categoryAmounts = (Map<String, Object>) requestData.get("categoryAmounts");
		Map<Integer, Integer> convertedCategoryAmounts = new HashMap<>();

		for (Map.Entry<String, Object> entry : categoryAmounts.entrySet()) {
			Integer categoryNum = Integer.parseInt(entry.getKey()); // String -> Integer 변환
			Object value = entry.getValue();
			Integer detailMoney = 0;
			if (value instanceof String) {
				detailMoney = value == "" ? 0 : Integer.parseInt((String) value);
			} else if (value instanceof Integer) {
				detailMoney = (Integer) value;
			}
			convertedCategoryAmounts.put(categoryNum, detailMoney);
		}

		// 서비스 호출
		moneyPlanService.saveMoneyPlan(memberNum, planMoney, convertedCategoryAmounts);

		return ResponseEntity.ok("용돈계획 저장 완료");
	}

	@GetMapping("/select/{memberNum}")
	public List<MoneyPlanDto> getMoneyPlan(@PathVariable("memberNum") int memberNum) {
		return moneyPlanService.getMoneyPlan(memberNum);
	}
}
