package com.shinhan.zoomoney.contract;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

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

import com.shinhan.zoomoney.member.MemberEntity;
import com.shinhan.zoomoney.member.MemberRepository;

@RestController
@RequestMapping("/contract")
public class ContractController {
	
    @Autowired
    ContractService contractService;
    
    @Autowired  // 주입 추가
    private MemberRepository memberRepository;
    

    // ✅ 부모가 서명 후 초안 저장 (PDF 생성 X)
    @PostMapping("/saveDraft")
    public String saveDraft(@RequestBody ContractDto contractDto, HttpSession session) {
        
    	//테스트용 부모 세션값
    	//Integer parentId = "user002".hashCode();
    	// 🔥 강제 하드코딩 테스트 (DB 조회 추가)
        Integer parentId = memberRepository.findByMemberId("user002")
                            .map(MemberEntity::getMemberNum)  // 해당 사용자의 ID 값을 가져옴
                            .orElseThrow(() -> new IllegalStateException("테스트용 사용자 'user002'를 찾을 수 없습니다."));
    	
    	// 세션값 받아오기
    	//Integer parentId = (Integer) session.getAttribute("userId"); // 부모의 세션 값

        if (parentId == null) {
            throw new IllegalStateException("세션 정보가 없습니다. 로그인 후 다시 시도하세요.");
        }

        return contractService.saveDraft(contractDto, parentId, contractDto.getContract_excelpath());
    }

    // ✅ 자녀가 서명 후 최종 PDF 생성
    @PostMapping("/complete")
    public String completeContract(@RequestParam int contractNum,
                                   @RequestParam String childSignature,
                                   HttpSession session) {
        Integer childId = (Integer) session.getAttribute("userId");

        if (childId == null) {
            throw new IllegalStateException("세션 정보가 없습니다. 로그인 후 다시 시도하세요.");
        }

        return contractService.completeContract(contractNum, childSignature, childId);
    }

    // ✅ 현재 유효한 계약서 조회
    @GetMapping("/select")
    public ContractEntity getValidContract(@RequestParam("childNum") int childNum) {
        return contractService.getValidContract(childNum)
                .orElseThrow(() -> new RuntimeException("유효한 계약서가 없습니다."));
    }
    // ✅ 계약서 비활성화 (계약 종료 시)
    @PutMapping("/disable/{contractNum}")
    public String disableContract(@PathVariable int contractNum) {
        return contractService.disableContract(contractNum);
    }

//	 //  1. 부모가 서명 후 초안 저장 (PDF 생성 X)
//    @PostMapping("/draft")
//    public String saveDraft(@RequestBody ContractDto contractDto, 
//                            @SessionAttribute("memberId") String memberId) {
//        return contractService.saveDraft(contractDto, memberId); // 세션 값 추가
//    }
//
//    //  2. 자녀가 서명한 후 최종 계약서 생성 (PDF 생성 O)
//    @PostMapping("/finalize")
//    public String finalizeContract(@RequestParam int contractId, 
//                                   @RequestParam String childSignature,
//                                   @SessionAttribute("memberId") String memberId) {
//        return contractService.finalizeContract(contractId, childSignature, memberId);
//    }
//
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
