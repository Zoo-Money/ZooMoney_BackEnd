package com.shinhan.zoomoney.account;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private ModelMapper modelMapper;

    // 사용자의 저금통 목록 조회
    public List<AccountDto> select(int memberNum) {
        List<AccountEntity> entityList = accountRepo.findAllByMember_MemberNum(memberNum);
        List<AccountDto> dtoList = entityList.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return dtoList;
    }

    // 저금통 상세 조회
    public AccountEntity selectById(int accountNum) {
        return accountRepo.findById(accountNum).orElse(null);
    }

    // 저금통 생성
    public void create(AccountDto dto) {
        AccountEntity entity = dtoToEntity(dto);
        accountRepo.save(entity);
    }

    // 저금통 금액 입금
    public void insert(int accountNum, int amount) {
        AccountEntity entity = accountRepo.findById(accountNum).orElse(null);

        if (entity != null) {
            entity.setAccountNow(entity.getAccountNow() + amount); // 저금통 금액 입금
            accountRepo.save(entity);
        }
    }

    // 저금통 상태(활성 여부) 변경
    public void close(int accountNum) {
        AccountEntity entity = accountRepo.findById(accountNum).orElse(null);

        if (entity != null) {
            entity.setAccountStatus(false); // 비활성화로 상태 변경
            accountRepo.save(entity);
        }
    }

    // 저금통 상태(해지 요청) 변경
    public void request(int accountNum, boolean request) {
        AccountEntity entity = accountRepo.findById(accountNum).orElse(null);

        if (entity != null) {
            entity.setAccountRequest(request); // 해지 요청 상태 변경
            accountRepo.save(entity);
        }
    }

    // DTO → Entity 변환
    public AccountEntity dtoToEntity(AccountDto dto) {
        return modelMapper.map(dto, AccountEntity.class);
    }

    // Entity → DTO 변환
    public AccountDto entityToDto(AccountEntity entity) {
        return modelMapper.map(entity, AccountDto.class);
    }
}
