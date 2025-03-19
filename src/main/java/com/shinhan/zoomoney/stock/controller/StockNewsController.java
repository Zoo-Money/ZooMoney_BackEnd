package com.shinhan.zoomoney.stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.zoomoney.stock.service.StockNewsService;

@RestController
@RequestMapping("/stock")
public class StockNewsController {

	@Autowired
	StockNewsService newsServiece;

	// 관련 뉴스 목록 가져오기
	@GetMapping("/getnews/{stockName}")
	public String searchNews(@PathVariable("stockName") String stockName) {
		String query = newsServiece.getStockName(stockName);
		if (query != null) {
			return newsServiece.searchNews(query);
		}
		return "해당되는 뉴스를 찾을 수 없습니다." + stockName;
	}
}
