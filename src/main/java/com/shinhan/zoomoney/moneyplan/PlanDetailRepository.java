package com.shinhan.zoomoney.moneyplan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanDetailRepository extends JpaRepository<PlanDetailEntity, Integer>{

	List<PlanDetailEntity> findByMoneyplan_PlanNum(int planNum);
//	List<PlanDetailEntity> findByMoneyPlan_PlanNumIn(List<Integer> planNums);
	
}
