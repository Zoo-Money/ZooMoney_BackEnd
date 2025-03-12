package com.shinhan.zoomoney.account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    // 사용자의 저금통 목록 조회
    @Query("SELECT e FROM AccountEntity e WHERE e.member.memberNum = :memberNum AND e.accountStatus = true")
    List<AccountEntity> findAllByMember_MemberNum(int memberNum);
}
