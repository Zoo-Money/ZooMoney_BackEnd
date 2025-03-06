package com.shinhan.zoomoney.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    // 저금통 생성
    public String create(AccountDto accountDto) {
        AccountEntity account = AccountEntity.builder()
                .account_num(accountDto.getAccount_num())
                //.child_num(accountDto.getChild_num())
                .account_name(accountDto.getAccount_name())
                .account_goal(accountDto.getAccount_goal())
                .account_now(accountDto.getAccount_now())
                .account_start(accountDto.getAccount_start())
                .account_end(accountDto.getAccount_end())
                .account_status(accountDto.isAccount_status())
                .build();

        accountRepository.save(account);
        return "Account created successfully!";
    }

    // 저금통 저금
    public String insert(int accountNum, int amount) {
        AccountEntity account = accountRepository.findById(accountNum).orElse(null);
        if (account != null) {
            account.setAccount_now(account.getAccount_now() + amount);
            accountRepository.save(account);
            return "Amount inserted successfully!";
        } else {
            return "Account not found!";
        }
    }

    // 저금통 상태변경
    public String modifyStatus(int accountNum, boolean status) {
        AccountEntity account = accountRepository.findById(accountNum).orElse(null);
        if (account != null) {
            account.setAccount_status(status);
            accountRepository.save(account);
            return "Account status modified successfully!";
        } else {
            return "Account not found!";
        }
    }
}
