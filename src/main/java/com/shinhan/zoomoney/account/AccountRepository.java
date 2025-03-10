package com.shinhan.zoomoney.account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    // 사용자의 저금통 목록 조회
    List<AccountEntity> findAllByMember_MemberNum(int memberNum);
}
