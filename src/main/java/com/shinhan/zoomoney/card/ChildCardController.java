package com.shinhan.zoomoney.card;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card")
public class ChildCardController {


    //카드 생성
    @PostMapping("/create")
    public String CardCreate() {
        return "create ok";
    }

    //카드 등록
    @PostMapping("/insert")
    public String CardInsert(@RequestBody ChildCardDto card) {
        return "insert ok";
    }

    //카드 이미지 변경
    @PutMapping("/modify")
    public String CardModify() {
        return "change ok";
    }

    //카드 거래내역 가져오기
    @GetMapping("/select")
    public List<UseHistoryDto> CardHistory() {

        return null;
    }


    @GetMapping("analysis")
    public List<UseHistoryDto> UseHistory(){
        return null;
    }
}
