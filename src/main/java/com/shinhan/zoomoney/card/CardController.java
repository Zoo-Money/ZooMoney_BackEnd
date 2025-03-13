package com.shinhan.zoomoney.card;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/card")
public class CardController {

	@Autowired
	private CardService cardService;

	// 카드 생성
	@PostMapping("/create")
	public String createCard(@RequestBody Map<String, Object> cardInfo) {
		try {
			System.out.println(cardInfo);
			cardService.createCard(cardInfo);
			return "카드 정보가 성공적으로 저장되었습니다.";
		} catch (Exception e) {
			e.printStackTrace();
			return "카드 정보 저장에 실패했습니다.";
		}
	}

	// 카드 조회
	@GetMapping("/get")
	public CardEntity getMyCards(HttpSession session) {
		// 세션에서 memberNum 가져오기

		// Integer memberNum = (Integer) session.getAttribute("member_num");
		Integer memberNum = 1;
		// 해당 회원의 카드 목록 조회
		CardEntity memberCards = cardService.getCardsByMemberNum(memberNum);

		// 세션에 카드 정보 저장
		session.setAttribute("tokenId", memberCards.getCardMetadata());
		session.setAttribute("card_num", memberCards.getCardNum());
		session.setAttribute("card_money", memberCards.getCardMoney());

		return memberCards;
	}

	// 카드 이미지 변경
	@PutMapping("/modify")
	public String CardModify() {
		return "change ok";
	}

	// 카드 거래내역 가져오기
	@GetMapping("/select")
	public List<UseHistoryDto> CardHistory() {

		return null;
	}

	@GetMapping("analysis")
	public List<UseHistoryDto> UseHistory() {
		return null;
	}

	// 카드 금액 변경
	@PutMapping("/change/{memberNum}")
	public void change(@PathVariable int memberNum, @RequestParam int amount) {
		cardService.change(memberNum, amount);
	}
}
