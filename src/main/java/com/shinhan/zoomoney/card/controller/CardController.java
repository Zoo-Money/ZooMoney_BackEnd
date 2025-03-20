package com.shinhan.zoomoney.card.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.zoomoney.card.dto.UseHistoryDto;
import com.shinhan.zoomoney.card.entity.CardEntity;
import com.shinhan.zoomoney.card.entity.UseHistoryEntity;
import com.shinhan.zoomoney.card.service.CardService;
import com.shinhan.zoomoney.card.service.UseHistoryService;
import com.shinhan.zoomoney.member.MemberService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/card")
public class CardController {

	@Autowired
	private CardService cardService;

	@Autowired
	private UseHistoryService useHistoryService;

	@Autowired
	private MemberService memberService;

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

	@GetMapping("/get")
	public ResponseEntity<?> getCardInfo(HttpSession session, @RequestParam("member_num") Integer memberNum) {
		// 해당 회원의 카드 목록 조회
		CardEntity memberCards = cardService.getCardsByMemberNum(memberNum);

		if (memberCards == null) {
		    return ResponseEntity.noContent().build();
		}

		// 세션에 카드 정보 저장
		session.setAttribute("tokenId", memberCards.getCardMetadata());
		session.setAttribute("card_num", memberCards.getCardNum());
		session.setAttribute("card_money", memberCards.getCardMoney());
		session.setAttribute("card_metadata", memberCards.getCardMetadata());

		return ResponseEntity.ok(memberCards);

	}

	// 카드 이미지 변경
	@PutMapping("/update")
	public ResponseEntity<String> updateCardDate(HttpSession session, @RequestBody Map<String, Object> cardData) {

		Integer memberNum = Integer.parseInt((String) cardData.get("member_num"));
		String cardNum = (String) cardData.get("card_num");

		cardService.updateCardDate(memberNum, cardNum);
		memberService.deductMemberPoint(memberNum);

		return ResponseEntity.ok("카드 업데이트 완료 되었습니다.");
	}

	// 카드 거래내역 가져오기
	@GetMapping("/select")
	public List<UseHistoryEntity> CardHistory(@RequestParam(value = "period", defaultValue = "all") String period,
			@RequestParam("member_num") Integer memberNum) {
		List<UseHistoryEntity> useHistoryList = useHistoryService.getHistoryByPeriod(period, memberNum);
		return useHistoryList;

	}

	@GetMapping("analysis")
	public List<UseHistoryDto> UseHistory() {

		return null;
	}

	// 카드 금액 변경
	@PutMapping("/change/{memberNum}")
	public void change(@PathVariable("memberNum") int memberNum, @RequestParam int amount) {
		cardService.change(memberNum, amount);
	}
}
