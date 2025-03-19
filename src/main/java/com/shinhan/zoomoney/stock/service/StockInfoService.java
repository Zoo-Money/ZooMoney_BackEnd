package com.shinhan.zoomoney.stock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.zoomoney.stock.entity.StockInfoEntity;
import com.shinhan.zoomoney.stock.repository.StockInfoRepository;

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
