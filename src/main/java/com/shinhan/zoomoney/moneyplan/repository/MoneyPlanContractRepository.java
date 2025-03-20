package com.shinhan.zoomoney.moneyplan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shinhan.zoomoney.contract.ContractEntity;

public interface MoneyPlanContractRepository extends JpaRepository<ContractEntity, Integer> {

	ContractEntity findTopByMember_MemberNumAndContractStatusTrueOrderByContractNumDesc(int memberNum);
}
