package com.shinhan.zoomoney.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    // 사용자의 저금통 목록 조회
    @GetMapping("/list/{memberNum}")
    public List<AccountDto> select(@PathVariable("memberNum") int memberNum) {
    	System.out.println("memberNum: "+memberNum);
    	List<AccountDto> result = accountService.select(memberNum);
    	System.out.println("result"+ result);
        return accountService.select(memberNum);
    }

    // 저금통 상세 조회
    @PostMapping("/select/{accountNum}")
    public AccountDto selectById(@PathVariable("accountNum") int accountNum) {
        return accountService.entityToDto(accountService.selectById(accountNum));
    }

    // 저금통 생성
    @PostMapping("/create")
    public void create(@RequestBody AccountDto dto) {
        accountService.create(dto);
    }

    // 저금통 금액 입금
    @PutMapping("/insert/{accountNum}")
    public void change(@PathVariable("accountNum") int accountNum, @RequestParam("amount") int amount) {
        accountService.insert(accountNum, amount);
    }

    // 저금통 상태(활성 여부) 변경
    @PutMapping("/close/{accountNum}")
    public void close(@PathVariable("accountNum") int accountNum) {
        accountService.close(accountNum);
    }

    // 저금통 상태(해지 요청) 변경
    @PutMapping("/request/{accountNum}")
    public void request(@PathVariable("accountNum") int accountNum, @RequestParam("request") boolean request) {
        accountService.request(accountNum, request);
    }
}
