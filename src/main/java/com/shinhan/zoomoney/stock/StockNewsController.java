package com.shinhan.zoomoney.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockNewsController {

	@Autowired
	StockNewsService newsServiece;
	
	@GetMapping("/getnews/{query}")
	public String searchNews(@PathVariable("query") String query) {
		return newsServiece.searchNews(query);
	}
}
