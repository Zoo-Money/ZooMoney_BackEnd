package com.shinhan.zoomoney.stock;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.shinhan.zoomoney.member.MemberEntity;
import com.shinhan.zoomoney.member.MemberRepository;
import com.shinhan.zoomoney.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
@RestController
@RequestMapping("/stock")
@CrossOrigin(origins = "http://localhost:3000")
public class StockController {
	private final StockService stockService;
	
	public StockController(StockService stockService) {
		this.stockService = stockService;
	}
	
	// 매수
	@PostMapping("/buy")
	public ResponseEntity<String> buyStock(@RequestBody BuyDto buyDto){
		System.out.println(buyDto);
		String result = stockService.buyStock(buyDto.memberNum, buyDto.stockId,
				buyDto.amount,buyDto.price);
		return ResponseEntity.ok(result);
	}
	
	
	
	// 매도
	@PostMapping("/sell")
	public ResponseEntity<String> sellStock(
			@RequestBody SellDto sellDto){
		String result = stockService.sellStock(sellDto.memberNum, sellDto.stockId, sellDto.amount, sellDto.price);
		System.out.println("Received Sell Request: " + sellDto);
		return ResponseEntity.ok(result);
	}
	
	// 돈 충전
	@PostMapping("/chargemoney")
	public ResponseEntity<String> chargeAllStockMoney(){
		stockService.chargeAllMembers();
		return ResponseEntity.ok("모든 멤버에게 1000000원 충전 완료!");
	}
	
	
	// 주식 시작하기 버튼 클릭 -> StockMoney 테이블에 member와 StockMoney추가
	@PostMapping("/start")
	public ResponseEntity<String> startStock(@RequestParam("member_num") int memberNum){
		try {
            String result = stockService.resetStockMoney(memberNum);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
	}
	
	// 보유한 주식 내역 조회
	@GetMapping("/owned")
	public ResponseEntity<List<OwnedStockDto>> getOWnedStock(@RequestParam("member_num")int memberNum){
		List<OwnedStockDto> result = stockService.getOwnedStocksByMember(memberNum);
		return ResponseEntity.ok(result);
	}
}
@Data
class BuyDto {
	int memberNum;
	String stockId;
	int amount;
	int price;
}

@Data
class SellDto {
	int memberNum;
	String stockId;
	int amount;
	int price;
}