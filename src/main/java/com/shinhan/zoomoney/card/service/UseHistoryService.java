package com.shinhan.zoomoney.card.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.zoomoney.card.entity.UseHistoryEntity;
import com.shinhan.zoomoney.card.repository.UseHistoryRepositiory;

@Service
public class UseHistoryService {
    @Autowired
    private UseHistoryRepositiory useHistoryRepositiory;

    public List<UseHistoryEntity> getHistoryByPeriod(String period, Integer memberNum) {
        LocalDateTime startDate;
        switch (period) {
            case "1":
                startDate = LocalDateTime.now().minusMonths(1);
                break;
            case "2":
                startDate = LocalDateTime.now().minusMonths(2);
                break;
            case "3":
                startDate = LocalDateTime.now().minusMonths(3);
                break;
            case "all":
            default:
                return useHistoryRepositiory.findByMember(memberNum); // 전체 조회
        }

        return useHistoryRepositiory.findByMemberAndPeriod(memberNum, startDate);
    }
}
