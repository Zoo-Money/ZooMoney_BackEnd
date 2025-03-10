package com.shinhan.zoomoney.stock;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockNewsController {

	@Autowired
	StockNewsService newsServiece;
	
	//관련 뉴스 목록 가져오기
	@GetMapping("/getnews/{query}")
	public String searchNews(@PathVariable("query") String query) {
		return newsServiece.searchNews(query);
	}
	
	//해당 뉴스 데이터 가져오기
	@GetMapping("/getnews/fulldata")
	public Map<String, Object> getNewsContent(@RequestParam(name="url") String url) {
		return newsServiece.getNewsContent(url);
	}
}
