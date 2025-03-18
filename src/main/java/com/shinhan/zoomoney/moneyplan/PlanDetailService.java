package com.shinhan.zoomoney.moneyplan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanDetailService {
	
	@Autowired
	PlanDetailRepository plandetailRepo;
	
	public void createPlanDetails(MoneyPlanEntity moneyPlan, List<PlanDetailEntity> planDetail) {
		for(PlanDetailEntity detail : planDetail) {
			detail.setMoneyplan(moneyPlan);
			plandetailRepo.save(detail);
		}
	}
}
