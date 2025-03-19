package com.shinhan.zoomoney.stock;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping("/detail/{infoNum}")
    public StockInfoDto getStockContent(@PathVariable int infoNum) {
        StockInfoEntity InfoDetailList = stockInfoService.findByInfoNum(infoNum);
        ModelMapper mapper = new ModelMapper();
        StockInfoDto dto = mapper.map(InfoDetailList, StockInfoDto.class);

        return dto;
    }
}
