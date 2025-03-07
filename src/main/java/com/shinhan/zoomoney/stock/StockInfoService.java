package com.shinhan.zoomoney.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockInfoService {

    @Autowired
    private StockInfoRepository stockInfoRepository;

    public List<StockInfoEntity> getAllTitles() {
        return stockInfoRepository.findAll();
    }

    public StockInfoEntity findByInfoNum(int infoNum) {
        return stockInfoRepository.findByInfoNum(infoNum);
    }
}
