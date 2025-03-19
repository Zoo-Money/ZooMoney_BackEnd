package com.shinhan.zoomoney.contract;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Integer> {

    // // 자녀의 ID로 초안 상태인 계약서 조회 (contract_status = false)
    Optional<ContractEntity> findFirstByMember_MemberNumAndContractStatus(int memberNum, boolean contractStatus);
    // //Optional<ContractEntity>
    // findFirstByMember_MemberNumAndContractStatus(Integer memberNum, Boolean
    // contractStatus);

    Optional<ContractEntity> findFirstByMember_MemberNumOrderByContractNumDesc(Integer memberNum);// 종현

    // 가장 최근 계약서 조회 (계약 상태가 true인 경우)
    Optional<ContractEntity> findFirstByMember_MemberNumAndContractStatusOrderByContractDateDesc(int childNum,
            boolean contractStatus);

    Optional<ContractEntity> findFirstByMember_MemberNumAndContractStatusOrderByContractNumDesc(int childNum,
            boolean contractStatus);

    // 과거 계약서 목록 조회
    List<ContractEntity> findAllByMember_MemberNumAndContractStatusOrderByContractDateDesc(int childNum,
            boolean contractStatus);

    // 최근 6개의 계약서 조회 (JPA 메서드 명 규칙 사용)
    Page<ContractEntity> findByMember_MemberNumOrderByContractNumDesc(int childNum, Pageable pageable);

}
