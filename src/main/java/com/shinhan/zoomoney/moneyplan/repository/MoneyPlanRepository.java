package com.shinhan.zoomoney.moneyplan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shinhan.zoomoney.moneyplan.entity.MoneyPlanEntity;

public interface MoneyPlanRepository extends JpaRepository<MoneyPlanEntity, Integer> {

    List<MoneyPlanEntity> findByMember_MemberNumOrderByPlanNumAsc(int memberNum);
}
