package com.shinhan.zoomoney.moneyplan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.zoomoney.moneyplan.entity.MoneyPlanEntity;
import com.shinhan.zoomoney.moneyplan.entity.PlanDetailEntity;
import com.shinhan.zoomoney.moneyplan.repository.PlanDetailRepository;

@Service
public class PlanDetailService {

	@Autowired
	PlanDetailRepository plandetailRepo;

	public void createPlanDetails(MoneyPlanEntity moneyPlan, List<PlanDetailEntity> planDetail) {
		for (PlanDetailEntity detail : planDetail) {
			detail.setMoneyplan(moneyPlan);
			plandetailRepo.save(detail);
		}
	}
}
