package com.shinhan.zoomoney.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockInfoController {
    @Autowired
    private StockInfoService stockInfoService;

    @GetMapping("/StockInfoAll")
    public List<StockInfoEntity> getAllTitles() {
        List<StockInfoEntity> stockInfoList = stockInfoService.getAllTitles();
        return stockInfoList;
    }
}
