package com.shinhan.zoomoney.moneyplan.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shinhan.zoomoney.contract.ContractEntity;
import com.shinhan.zoomoney.member.MemberEntity;
import com.shinhan.zoomoney.member.MemberRepository;
import com.shinhan.zoomoney.moneyplan.dto.MoneyPlanDto;
import com.shinhan.zoomoney.moneyplan.dto.PlanDetailDto;
import com.shinhan.zoomoney.moneyplan.entity.CategoryEntity;
import com.shinhan.zoomoney.moneyplan.entity.MoneyPlanEntity;
import com.shinhan.zoomoney.moneyplan.entity.PlanDetailEntity;
import com.shinhan.zoomoney.moneyplan.repository.CategoryRepository;
import com.shinhan.zoomoney.moneyplan.repository.MoneyPlanContractRepository;
import com.shinhan.zoomoney.moneyplan.repository.MoneyPlanRepository;
import com.shinhan.zoomoney.moneyplan.repository.PlanDetailRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MoneyPlanService {

	@Autowired
	MoneyPlanRepository moneyplanRepo;

	@Autowired
	PlanDetailRepository plandetailRepo;

	@Autowired
	MoneyPlanContractRepository contractRepo;

	@Autowired
	MemberRepository memberRepo;

	@Autowired
	CategoryRepository categoryRepo;

	// @Autowired
	// PlanDetailService plandetailService;

	// 용돈계약서에서 지급 금액 가져오기
	public int getAllowance(int memberNum) {
		ContractEntity contract = contractRepo
				.findTopByMember_MemberNumAndContractStatusTrueOrderByContractNumDesc(memberNum);
		if (contract != null) {
			return contract.getContractMoney();
		}
		return 0;
	}

	// 용돈 계획 테이블에 저장
	@Transactional
	public void saveMoneyPlan(int memberNum, int planMoney, Map<Integer, Integer> categoryAmount) {		
		//memberNum으로 entity 찾기
		MemberEntity member = memberRepo.findById(memberNum).orElse(null);

		// 용돈계획저장
		MoneyPlanEntity moneyPlan = MoneyPlanEntity.builder().member(member).planDate(new Date()).planMoney(planMoney)
				.planStatus(true).build();
		MoneyPlanEntity savedPlan = moneyplanRepo.save(moneyPlan);

		// 용돈상세계획 저장
		for (Map.Entry<Integer, Integer> entry : categoryAmount.entrySet()) {
			int categoryNum = entry.getKey();
			int detailMoney = entry.getValue();
			CategoryEntity category = categoryRepo.findByCategoryNum(categoryNum);
			PlanDetailEntity detail = PlanDetailEntity.builder().moneyplan(savedPlan).category(category)
					.detailMoney(detailMoney).build();
			plandetailRepo.save(detail);
		}

	}

	
	//용돈 계획 조회
	public List<MoneyPlanDto> getMoneyPlan(int memberNum){

		List<MoneyPlanEntity> moneyPlans = moneyplanRepo.findByMember_MemberNumOrderByPlanNumAsc(memberNum);
		List<MoneyPlanDto> moneyPlanDTOList = new ArrayList<>();
		for (MoneyPlanEntity moneyPlan : moneyPlans) {
			MoneyPlanDto moneyPlanDTO = MoneyPlanDto.builder()
					.plan_num(moneyPlan.getPlanNum())
					.plan_date(moneyPlan.getPlanDate())
					.plan_money(moneyPlan.getPlanMoney())
					.build();
			List<PlanDetailEntity> planDetails = plandetailRepo.findByMoneyplan_PlanNum(moneyPlan.getPlanNum());
			List<PlanDetailDto> planDetailDTOList = new ArrayList<>();
			for (PlanDetailEntity planDetail : planDetails) {
				PlanDetailDto planDetailDTO = PlanDetailDto.builder()
						.detail_num(planDetail.getDetailNum())
						.category_num(planDetail.getCategory().getCategoryNum())
						.detail_money(planDetail.getDetailMoney())
						.plan_num(planDetail.getMoneyplan().getPlanNum())
						.build();
				planDetailDTOList.add(planDetailDTO);
			}
			moneyPlanDTO.setPlanDetails(planDetailDTOList);
			moneyPlanDTOList.add(moneyPlanDTO);
		}
		return moneyPlanDTOList;
	}

}
