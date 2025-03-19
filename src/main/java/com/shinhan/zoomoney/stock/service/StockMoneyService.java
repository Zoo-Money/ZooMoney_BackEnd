package com.shinhan.zoomoney.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.zoomoney.stock.repository.StockMoneyRepository;

@Service
public class StockMoneyService {
    @Autowired
    StockMoneyRepository stockMoneyRepository;

    public boolean hasMemberNumCheck(int memberNum) {
        return stockMoneyRepository.existsByMember_MemberNum(memberNum);
    }
}
