package com.shinhan.zoomoney.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    // 저금통 생성
    @PostMapping("/create")
    public String create(@RequestBody AccountDto account) {
        return accountService.create(account);
    }

    // 저금통 저금
    @PutMapping("/insert")
    public String insert(@RequestParam int accountNum, @RequestParam int amount) {
        return accountService.insert(accountNum, amount);
    }

    // 저금통 상태변경
    @PutMapping("/modify")
    public String modify(@RequestParam int accountNum, @RequestParam boolean status) {
        return accountService.modifyStatus(accountNum, status);
    }
}
