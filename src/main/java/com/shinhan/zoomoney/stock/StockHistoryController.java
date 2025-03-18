package com.shinhan.zoomoney.stock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock/history")
public class StockHistoryController {

	@Autowired
	StockHistoryService historyService;
	
	@GetMapping("/list/{memberNum}")
	public List<StockHistoryDto> selectAllHistory(@PathVariable("memberNum") int memberNum){
		return historyService.selectStockHitory(memberNum);
	}
}
