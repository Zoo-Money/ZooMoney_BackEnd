package com.shinhan.zoomoney.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockInfoService {

    @Autowired
    private StockInfoRepository stockInfoRepository;

    // 제목 목록을 가져오는 메서드
    public List<StockInfoEntity> getAllTitles() {
        return stockInfoRepository.findAll();
    }
}
