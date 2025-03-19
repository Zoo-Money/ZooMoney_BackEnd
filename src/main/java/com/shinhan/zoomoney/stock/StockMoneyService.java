package com.shinhan.zoomoney.stock;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockMoneyService {
    @Autowired
    StockMoneyRepository stockMoneyRepository;
    public boolean hasMemberNumCheck(int memberNum) {
        return stockMoneyRepository.existsByMember_MemberNum(memberNum);
    }
}
