package com.shinhan.zoomoney.stock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.zoomoney.stock.dto.StockHistoryBackupDto;
import com.shinhan.zoomoney.stock.dto.StockHistoryDto;
import com.shinhan.zoomoney.stock.service.StockHistoryService;

@RestController
@RequestMapping("/stock/history")
public class StockHistoryController {

	@Autowired
	StockHistoryService historyService;

	@GetMapping("/list/{memberNum}")
	public List<StockHistoryBackupDto> selectAllHistory(@PathVariable("memberNum") int memberNum) {
		return historyService.selectStockHitory(memberNum);
	}
}
