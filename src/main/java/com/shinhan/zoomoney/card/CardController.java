package com.shinhan.zoomoney.card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/card")
public class CardController {

	@Autowired
	private  CardService cardService;

    //카드 생성
    @PostMapping("/create")
    public String CardCreate() {
        return "create ok";
    }

    //카드 조회
    @GetMapping("/get")
    public List<CardEntity> getMyCards(HttpSession session) {
    	// 세션에서 memberNum 가져오기
        Integer memberNum = (Integer) session.getAttribute("member_num");
        // 해당 회원의 카드 목록 조회
        List<CardEntity> memberCards = cardService.getCardsByMemberNum(memberNum);

        // 세션에 카드 정보 저장
        List<Map<String, Object>> cardSessionData = new ArrayList<>();
        for (CardEntity card : memberCards) {
            Map<String, Object> cardData = Map.of(
                "cardNum", card.getCardNum(),
                "cardMetadata", card.getCardMetadata(),
                "cardMoney", card.getCardMoney()
            );
            cardSessionData.add(cardData);
        }
        session.setAttribute("my_cards", cardSessionData);

        return memberCards;
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
