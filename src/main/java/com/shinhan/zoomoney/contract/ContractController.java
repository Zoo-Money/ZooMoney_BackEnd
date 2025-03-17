package com.shinhan.zoomoney.contract;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.SessionAttribute;

import com.shinhan.zoomoney.card.CardEntity;
import com.shinhan.zoomoney.card.CardService;
import com.shinhan.zoomoney.member.MemberEntity;
import com.shinhan.zoomoney.member.MemberRepository;
import com.shinhan.zoomoney.member.MemberService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


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
    
    
    // 부모 ID를 기반으로 자녀 목록 조회
    @GetMapping("/getChildByParent")
    public ResponseEntity<List<MemberEntity>> getChildByParent(@RequestParam("parentId") int parentId){
    	
    	//부모ID 세션사용시
//  public ResponseEntity<List<MemberEntity>> getChildByParent(HttpSession session) {
//    	 Integer parentId = (Integer) session.getAttribute("parentId");  // 세션에서 부모 ID 불러오기
//
//    	 if (parentId == null) {
//    	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);    	
    	
    	MemberEntity memberParent = MemberEntity.builder().memberNum(parentId).build();
    	List<MemberEntity> children = memberRepository.findByMemberParent(memberParent);
    	System.out.println("@@부모ID 받아옴/" +memberParent);
    	System.out.println("@@children 받아옴/" +children);
    	
    	if (children.isEmpty()) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
          }
    	
    	return ResponseEntity.ok(children);
    }
    
    
    // 자녀 카드 잔액 조회
	@GetMapping("/child/money")
	public ResponseEntity<?> getMyCards(@RequestParam("memberNum") Integer memberNum, HttpSession session) {
	    // memberNum 파라미터를 받아서 해당 멤버의 카드 조회
	    if (memberNum == null) {
	        return ResponseEntity.badRequest().body("멤버 번호가 누락되었습니다.");
	    }
	    // 해당 회원의 카드 목록 조회
	    CardEntity memberCards = cardService.getCardsByMemberNum(memberNum);
	    if (memberCards == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 멤버의 카드 정보를 찾을 수 없습니다.");
	    }
	    return ResponseEntity.ok(memberCards);
	}

	// 부모정보조회(용돈계약서-부모이름)
	@GetMapping("/parentInfo")
	public ResponseEntity<?> getParentInfo(@RequestParam("parentId") int parentId){
		try {
			String parentName =contractService.getParentName(parentId);
			return ResponseEntity.ok().body(Map.of("parentName",parentName));
		}catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("부모 정보를 찾을 수 없습니다.");
	    }catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버오류발생");
		}
	}
	
	
	// 아이정보조회(용돈계약서-아이이름)
	@GetMapping("/childInfo")
	public ResponseEntity<?> getChildInfo(@RequestParam("childId") int childId){
		try {
			String childName =contractService.getChildName(childId);
			return ResponseEntity.ok().body(Map.of("childName",childName));
		}catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("아이 정보를 찾을 수 없습니다.");
	    }catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버오류발생");
		}
	}
	
	
    
    
    // 부모가 서명 후 초안 저장 (PDF 생성 X)
    @PostMapping("/saveDraft")
    public String saveDraft(@RequestBody ContractDto contractDto, HttpSession session) {
        
    	//강제 하드코딩 테스트 (DB 조회 추가)
        Integer parentId = memberRepository.findByMemberId("user002")
                            .map(MemberEntity::getMemberNum)  // 해당 사용자의 ID 값을 가져옴
                            .orElseThrow(() -> new IllegalStateException("테스트용 사용자 'user002'를 찾을 수 없습니다."));
    	
//    	 세션값 받아오기
//    	Integer parentId = (Integer) session.getAttribute("userId"); // 부모의 세션 값
//        if (parentId == null) {
//            throw new IllegalStateException("세션 정보가 없습니다. 로그인 후 다시 시도하세요.");
//        }
        
//        System.out.println(" contract_content: " + contractDto.getContract_content());
//        System.out.println(" contract_date: " + contractDto.getContract_date());
//        System.out.println(" contract_excelpath: " + contractDto.getContract_excelpath());
//        System.out.println(" contract_filepath: " + contractDto.getContract_filepath());
//        System.out.println("parentId:" + parentId); // 결과 : 2
       
        return contractService.saveDraft(contractDto, parentId, contractDto.getContract_excelpath());
    }
    
    
    
    // 부모가 작성한 용돈계약서 내용확인
    @GetMapping("/getDetails")
    public ResponseEntity<ContractEntity> getContractDetails(@RequestParam("childId") int childId,HttpSession session) {
//        Integer childId = (Integer) session.getAttribute("childNum"); //  로그인한 자녀의 ID를 세션에서 가져옴
        
//    	//강제 하드코딩 테스트 (DB 조회 추가)
//        Integer childId = memberRepository.findByMemberId("user001")
//                            .map(MemberEntity::getMemberNum)  // 해당 사용자의 ID 값을 가져옴
//                            .orElseThrow(() -> new IllegalStateException("테스트용 아이사용자 'user001'를 찾을 수 없습니다."));

       System.out.println("내용확인시 포함된[LOG] 조회된 childId: " + childId);


//        if (childId == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); //아이 ID (세션값)이 없으면 오류 반환
//        }

     //  자녀 ID를 기준으로 '초안' 계약서 조회
        Optional<ContractEntity> contractOpt = contractRepository.findFirstByMember_MemberNumAndContractStatus(childId, false);
 
        if (contractOpt.isPresent()) {
            return ResponseEntity.ok(contractOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
       
    }

    
 
      //  자녀가 서명 후 최종 PDF 생성
	@PostMapping("/complete")
	public String completeContract(@RequestBody Map<String, Object> contractData, HttpSession session) {
	
	    // JSON 데이터에서 contractNum, childSignature 추출
//	    int contractNum = (int) contractData.get("contractNum");
		int childNum = (int) contractData.get("childNum");
	    String childSignature = (String) contractData.get("childSignature");
//	    System.out.println("아이게뭐야1"+contractNum);
	    System.out.println("아이게뭐야2"+childSignature);
	    System.out.println("아이게뭐야3"+childNum);
	    
	    //정상작동
//	    Integer childId = (Integer) session.getAttribute("member_num");
	    //테스트용
	    Integer childId =1;
//	    System.out.print(childId+"이거에요 이거에요");
	    
	    ContractEntity contractEntity = contractRepository.findFirstByMember_MemberNumOrderByContractNumDesc(childId)
	    		.orElseThrow(() -> new IllegalStateException("해당 아이디로 계약서가 조회되지 않습니다."));
	    
//	    // 자녀 ID 조회 (테스트용 하드코딩)
//	    Integer childId = memberRepository.findByMemberId("user003")
//	            .map(MemberEntity::getMemberNum)
//	            .orElseThrow(() -> new IllegalStateException("테스트용 아이사용자 'user003'를 찾을 수 없습니다."));
	
	    // 자녀 ID 유효성 검사
//	    if (childId == null) {
//	        throw new IllegalStateException("세션 정보가 없습니다. 로그인 후 다시 시도하세요.");
//	    }
	
	    // 서비스 호출 (서명 저장 및 PDF 생성)
//	    return contractService.completeContract(contractNum, childSignature, childId);
	    return contractService.completeContract(childNum, childSignature);
	}
	
	@GetMapping("/contract/pdf/{fileName}")
	@CrossOrigin(origins = "http://localhost:3000") // ✅ CORS 설정 추가
	public ResponseEntity<Resource> getContractPdf(@PathVariable String fileName) {
		System.out.println("@@@@파일이름은"+fileName);
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
    public ContractEntity getValidContract(@RequestParam("childNum") int childNum) {
        return contractService.getValidContract(childNum)
                .orElseThrow(() -> new RuntimeException("유효한 계약서가 없습니다."));
    }
    // 최신 유효한 계약서 조회 API
    @GetMapping("/latest")
    public ResponseEntity<String> getLatestContract(@RequestParam("childNum") int childNum) {
        Optional<ContractEntity> latestContract = contractRepository
            .findFirstByMember_MemberNumAndContractStatusOrderByContractNumDesc(childNum, true);
        System.out.println("##최신유효계약서조회 childNum:"+childNum);
        System.out.println("##최신유효계약서조회 data"+latestContract);
        

        if (latestContract.isPresent()) {
            return ResponseEntity.ok(latestContract.get().getContractFilepath());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유효한 계약서가 없습니다.");
        }
    }
    // 과거 계약서 목록 조회 API
    @GetMapping("/history")
    public ResponseEntity<List<ContractEntity>> getPastContracts(@RequestParam("childNum") int childNum) {
        List<ContractEntity> pastContracts = contractRepository
            .findAllByMember_MemberNumAndContractStatusOrderByContractDateDesc(childNum, false);

        if (!pastContracts.isEmpty()) {
            return ResponseEntity.ok(pastContracts);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    
    
    // 계약서 비활성화 (계약 종료 시)
    @PutMapping("/disable/{contractNum}")
    public String disableContract(@PathVariable int contractNum) {
        return contractService.disableContract(contractNum);
    }
    
    
    // 부모 계좌조회(송금페이지)
    @GetMapping("/account/{memberId}")
    public ResponseEntity<?> getAccountInfo(@PathVariable("memberId") String memberId){
    	   String accountInfo = memberService.getMemberAccount(memberId);

    	    if (accountInfo.equals("계좌 정보가 없습니다.")) {
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("B_CC)계좌 정보를 찾을 수 없습니다.");
    	    } else {
    	        return ResponseEntity.ok().body(Map.of("member_account", accountInfo));
    	    }
    }

    // 용돈 송금하기
    @PutMapping("/sendAllowance/{childNum}")
    public ResponseEntity<?> sendAllowance(@PathVariable("childNum") int childNum, @RequestBody Map<String, Integer> requestData){
    	int amount = requestData.get("amount");
    	contractService.sendAllowance(childNum, amount);
    	return ResponseEntity.ok("송금이 완료되었습니다.");
    }
    

//    //  3. 현재 유효한 계약서 조회
//    @GetMapping("/select")
//    public ContractEntity getValidContract(@SessionAttribute("memberId") String memberId) {
//        return contractService.getValidContract(memberId);
//    }
//
//    //  4. 과거 계약서 목록 조회
//    @GetMapping("/select/history")
//    public List<ContractEntity> getPastContracts(@SessionAttribute("memberId") String memberId) {
//        return contractService.getPastContracts(memberId);
//    }
//
//    //  5. 계약서 비활성화
//    @PutMapping("/disable/{contractNum}")
//    public String disableContract(@PathVariable int contractNum) {
//        return contractService.disableContract(contractNum);
//    }
//	

}
