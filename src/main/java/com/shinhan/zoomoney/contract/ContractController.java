package com.shinhan.zoomoney.contract;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.zoomoney.card.entity.CardEntity;
import com.shinhan.zoomoney.card.service.CardService;
import com.shinhan.zoomoney.member.MemberEntity;
import com.shinhan.zoomoney.member.MemberRepository;
import com.shinhan.zoomoney.member.MemberService;

@RestController
@RequestMapping("/contract")
public class ContractController {

	@Autowired
	ContractService contractService;

	@Autowired
	MemberService memberService;

	@Autowired
	CardService cardService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ContractRepository contractRepository;

	// 자녀 목록 조회
	@GetMapping("/getChildByParent")
	public ResponseEntity<List<MemberEntity>> getChildByParent(@RequestParam("parentId") int memberNum) {
		MemberEntity memberParent = MemberEntity.builder().memberNum(memberNum).build();
		List<MemberEntity> children = memberRepository.findByMemberParent(memberParent);
		System.out.println("@@log memberParent/" + memberParent);
		System.out.println("@@log children/" + children);
		if (children.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(children);
	}

	// 부모페이지에서 자녀 카드잔액 조회
	@GetMapping("/child/money")
	public ResponseEntity<?> getMyCards(@RequestParam("memberNum") Integer memberNum) {
		if (memberNum == null) {
			return ResponseEntity.badRequest().body("멤버 번호가 누락되었습니다.");
		}
		CardEntity memberCards = cardService.getCardsByMemberNum(memberNum);
		if (memberCards == null) {
			memberCards = CardEntity.builder().cardMoney(0).build();
		}
		return ResponseEntity.ok(memberCards);
	}

	// 부모정보조회(용돈계약서-부모이름)
	@GetMapping("/parentInfo")
	public ResponseEntity<?> getParentInfo(@RequestParam("parentId") int memberNum) {
		try {
			String parentName = contractService.getParentName(memberNum);
			return ResponseEntity.ok().body(Map.of("parentName", parentName));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("부모 정보를 찾을 수 없습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버오류발생");
		}
	}

	// 아이정보조회(용돈계약서-아이이름)
	@GetMapping("/childInfo")
	public ResponseEntity<?> getChildInfo(@RequestParam("childId") int memberNum) {
		try {
			String childName = contractService.getChildName(memberNum);
			return ResponseEntity.ok().body(Map.of("childName", childName));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("아이 정보를 찾을 수 없습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버오류발생");
		}
	}

	// 부모가 서명 후 초안 저장 (PDF 생성 X)
	@PostMapping("/saveDraft")
	public String saveDraft(@RequestBody ContractDto contractDto, @RequestParam("parentId") Integer memberNum) {
		return contractService.saveDraft(contractDto, memberNum, contractDto.getContract_excelpath());
	}

	// 부모가 작성한 용돈계약서 내용확인
	@GetMapping("/getDetails")
	public ResponseEntity<ContractEntity> getContractDetails(@RequestParam("childId") int memberNum) {
		// 자녀 ID를 기준으로 '초안' 계약서 조회
		Optional<ContractEntity> contractOpt = contractRepository
				.findFirstByMember_MemberNumAndContractStatus(memberNum, false);

		if (contractOpt.isPresent()) {
			return ResponseEntity.ok(contractOpt.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

	}

	// 자녀가 서명 후 최종 PDF 생성
	@PostMapping("/complete")
	public String completeContract(@RequestBody Map<String, Object> contractData) {

		// JSON 데이터에서 childNum, childSignature 추출
		int childNum = (int) contractData.get("childNum");
		String childSignature = (String) contractData.get("childSignature");

		// 서비스 호출 (서명 저장 및 PDF 생성)
		return contractService.completeContract(childNum, childSignature);
	}

	@GetMapping("/contract/pdf/{fileName}")
	@CrossOrigin(origins = "http://localhost:3000") // CORS 설정 추가
	public ResponseEntity<Resource> getContractPdf(@PathVariable("fileName") String fileName) {
		System.out.println("@@@@파일이름은" + fileName);
		try {
			Path pdfPath = Paths.get("src/main/resources/contract_pdf/" + fileName);
			Resource resource = new ByteArrayResource(Files.readAllBytes(pdfPath));

			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_PDF)
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName)
					.body(resource);

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	// 현재 유효한 계약서 조회
	@GetMapping("/select")
	public ContractEntity getValidContract(@RequestParam("childNum") int memberNum) {
		return contractService.getValidContract(memberNum)
				.orElseThrow(() -> new RuntimeException("유효한 계약서가 없습니다."));
	}

	// 최신 유효한 계약서 조회 API
	@GetMapping("/latest")
	public ResponseEntity<String> getLatestContract(@RequestParam("childNum") int memberNum) {
		Optional<ContractEntity> latestContract = contractRepository
				.findFirstByMember_MemberNumAndContractStatusOrderByContractNumDesc(memberNum, true);

		if (latestContract.isPresent()) {
			return ResponseEntity.ok(latestContract.get().getContractFilepath());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유효한 계약서가 없습니다.");
		}
	}

	// 과거계약서조회
	@GetMapping("/pastContracts/{childNum}")
	public ResponseEntity<List<ContractEntity>> getPastContracts(@PathVariable("childNum") int memberNum) {
		List<ContractEntity> contracts = contractService.getPastContracts(memberNum);

		if (contracts.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
		}

		return ResponseEntity.ok(contracts);
	}

	// 부모 계좌조회(송금페이지)
	@GetMapping("/account/{memberId}")
	public ResponseEntity<?> getAccountInfo(@PathVariable("memberId") String memberId) { // Id로 조회. memberNum 아님.
		String accountInfo = memberService.getMemberAccount(memberId);

		if (accountInfo.equals("계좌 정보가 없습니다.")) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("B_CC)계좌 정보를 찾을 수 없습니다.");
		} else {
			return ResponseEntity.ok().body(Map.of("member_account", accountInfo));
		}
	}

	// 용돈 송금하기
	@PutMapping("/sendAllowance/{childNum}")
	public ResponseEntity<?> sendAllowance(@PathVariable("childNum") int memberNum,
			@RequestBody Map<String, Integer> requestData) {
		int amount = requestData.get("amount");
		contractService.sendAllowance(memberNum, amount);
		return ResponseEntity.ok("송금이 완료되었습니다.");
	}

}
