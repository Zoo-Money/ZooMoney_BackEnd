package com.shinhan.zoomoney.stock;

import java.util.Enumeration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shinhan.zoomoney.member.MemberEntity;
import com.shinhan.zoomoney.member.MemberRepository;
import com.shinhan.zoomoney.member.MemberService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/stock")
public class StockController {
	private final StockService stockService;

	@Autowired
	StockMoneyService stockMoneyService;
	
	public StockController(StockService stockService) {
		this.stockService = stockService;
	}
	
	// 매도
	@PostMapping("/buy")
	public ResponseEntity<String> buyStock(
			@RequestParam(name="memberNum") int memberNum,
			@RequestParam(name="stockNum") int stockNum,
			@RequestParam(name="amount") int amount,
			@RequestParam(name="price") int price){
		String result = stockService.buyStock(memberNum, stockNum, amount, price);
		return ResponseEntity.ok(result);
	}
	
	// 매수
	@PostMapping("/sell")
	public ResponseEntity<String> sellStock(
			@RequestParam(name="memberNum") int memberNum,
			@RequestParam(name="stockNum") int stockNum,
			@RequestParam(name="amount") int amount,
			@RequestParam(name="price") int price){
		String result = stockService.sellStock(memberNum, stockNum, amount, price);
		return ResponseEntity.ok(result);
	}
	
	// 돈 충전
	@PostMapping("/chargemoney")
	public ResponseEntity<String> chargeAllStockMoney(){
		stockService.chargeAllMembers();
		return ResponseEntity.ok("모든 멤버에게 1000000원 충전 완료!");
	}

	@PostMapping("/userStatus")
	public ResponseEntity<Integer> checkChildNum(@RequestBody StockMoneyDto stockMoneyDto) {
		int memberNum = stockMoneyDto.getMemberNum();
		boolean exists = stockMoneyService.hasMemberNumCheck(memberNum);
		int resultCode = exists ? 1 : 0;
		return ResponseEntity.ok(resultCode);
	}

	
	// 주식 시작하기 버튼 클릭 -> StockMoney 테이블에 member와 StockMoney추가
	@PostMapping("/start")
	public ResponseEntity<String> startStock(@RequestBody StockMoneyDto stockMoneyDto){
		int memberNum = stockMoneyDto.getMemberNum();
		try {
            String result = stockService.resetStockMoney(memberNum);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
	}
	
	// 보유한 주식 내역 조회
	@GetMapping("/owned")
	public ResponseEntity<List<OwnedStockDto>> getOWnedStock(@RequestParam("memberNum")int memberNum){
		List<OwnedStockDto> result = stockService.getOwnedStocksByMember(memberNum);
		return ResponseEntity.ok(result);
	}
}
