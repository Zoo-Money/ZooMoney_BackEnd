package com.shinhan.zoomoney.stock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.zoomoney.stock.dto.StockResultDto;
import com.shinhan.zoomoney.stock.service.StockResultService;

@RestController
@RequestMapping("/stock/result")
public class StockResultController {

	@Autowired
	StockResultService resultService;

	@GetMapping("/list/{memberNum}")
	public List<StockResultDto> selectAllResult(@PathVariable("memberNum") int memberNum) {
		return resultService.selectAllByMemberNum(memberNum);
	}
}
