 package com.shinhan.zoomoney.contract;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shinhan.zoomoney.member.MemberEntity;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Integer>{
//DB에서 계약서 데이터를 가져오기 위한 JPA Repository
	// 유효한 계약서 조회 (contract_status = true)
	//Optional<ContractEntity> findFirstByChildAndContractStatus(MemberEntity member, boolean contractStatus);

    // 과거 계약서 목록 조회 (contract_status = false)
    //List<ContractEntity> findAllByChild_ChildNumAndContractStatus(MemberEntity member, boolean contractStatus);
	

//    Optional<ContractEntity> findFirstByChildAndContractStatus(MemberEntity child, boolean status);
//    List<ContractEntity> findAllByChild_MemberNumAndContractStatus(int childNum, boolean status);
	
    // ✅ 현재 유효한 계약서 조회 (계약 상태가 true인 경우)
    Optional<ContractEntity> findFirstByMember_MemberNumAndContractStatus(int childNum, boolean contractStatus);

    // ✅ 특정 계약서 ID로 조회
    Optional<ContractEntity> findById(int contractNum);

    // ✅ 특정 부모의 모든 계약서 조회
    List<ContractEntity> findAllByMember_MemberNum(int parentId);

    // ✅ 과거 계약서 목록 조회 (계약 상태가 false인 경우)
    List<ContractEntity> findAllByMember_MemberNumAndContractStatus(int parentId, boolean contractStatus);

    // ✅ 특정 부모 + 특정 자녀의 유효한 계약서 조회 (만약 특정 아이만 필터링할 경우)
    @Query("SELECT c FROM ContractEntity c WHERE c.member.memberNum = :parentId AND c.contractContent LIKE %:childName% AND c.contractStatus = true")
    Optional<ContractEntity> findValidContractByChildName(@Param("parentId") int parentId, @Param("childName") String childName);

	
    
}

 