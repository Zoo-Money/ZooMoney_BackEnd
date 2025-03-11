package com.shinhan.zoomoney.contract;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.shinhan.zoomoney.member.MemberEntity;
import com.shinhan.zoomoney.member.MemberRepository;
import java.util.Optional;

@Service
public class ContractService {
	//계약서 생성, 조회, 서명 추가 등의 핵심 로직 처리
	//contract_status 변경, PDF 생성 로직 추가 가능
	
//	@Autowired
//	private ContractRepository contractRepository;
//	
//	@Autowired
//	private MemberRepository childRepository;
//	
//	
//	
//	
//	//계약서 생성
//	public ContractEntity createContract(ContractDto dto) {
//		
//		// ChildEntity 객체를 생성하여 넣어야 함
//		//ContractEntity에는 자녀 정보가 @ManyToOne으로 연결되어 있어서, child_num 대신 ChildEntity 객체를 저장해야 함
//	    MemberEntity memberEntity = new MemberEntity();
//	    memberEntity.setChildNum(dto.getChild_num()); // DTO에서 child_num 가져와 설정
//		
//		ContractEntity contract = ContractEntity.builder()
//									.child(memberEntity)  // child_num(int) 대신 child(ChildEntity) 사용
//									.contractMoney(dto.getContract_money())
//									.contractContent(dto.getContract_content())
//					                .contractStatus(true) //계약 생성시 활성화 상태로 저장
//					                .contractFilepath(dto.getContract_filepath())
//					                .contractDate(dto.getContract_date())
//					                .contractProvide(dto.getContract_provide())
//									.build();
//		return contractRepository.save(contract);
//	}
//	
//	
//
//    // ✅ 2. 현재 유효한 계약서 조회 (contract_status = true)
//    public Optional<ContractEntity> getValidContract(int childNum) {
//        //return contractRepository.findFirstByChild_ChildNumAndContractStatus(childNum, true);
//    	   // 1. childNum을 기반으로 ChildEntity 가져오기
//        Optional<ChildEntity> childEntity = childRepository.findById(childNum);
//        
//        // 2. 만약 존재하지 않는다면, 빈 결과 반환
//        if (childEntity.isEmpty()) {
//            return Optional.empty();
//        }
//
//        // 3. childEntity를 사용하여 ContractEntity 검색
//        return contractRepository.findFirstByChildAndContractStatus(childEntity.get(), true);
//
//
//    }
//
//    
//
//    // ✅ 3. 과거 계약서 목록 조회 (contract_status = false)
//    public List<ContractEntity> getPastContracts(int childNum) {
//        return contractRepository.findAllByChild_ChildNumAndContractStatus(childNum, false);
//    }
//
//    // ✅ 4. 계약서 비활성화 (contract_status = false)
//    public String disableContract(int contractNum) {
//        Optional<ContractEntity> contractOpt = contractRepository.findById(contractNum);
//        if (contractOpt.isPresent()) {
//            ContractEntity contract = contractOpt.get();
//            contract.setContractStatus(false);
//            contractRepository.save(contract);
//            return "계약서가 비활성화되었습니다.";
//        }
//        return "계약서를 찾을 수 없습니다.";
//    }
//	
//	//계약서 서명 추가(부모서명 저장)
//	public String addParentSignature(int contractNum, String imgPath) {
//		Optional<ContractEntity> contractOpt = contractRepository.findById(contractNum);
//		if(contractOpt.isPresent()) {
//			ContractEntity contract = contractOpt.get();
//			contract.setContractExcelpath(imgPath);
//			contract.setContractStatus(true);
//			contractRepository.save(contract);
//			return "아이 서명이 추가되었습니다.";
//		}
//		return "계약서를 찾을 수 없습니다.";
//		
//	}
//	
	
	@Autowired
    private ContractRepository contractRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private SignatureService signatureService;

    // ✅ 부모가 서명 후 초안 저장 (PDF 생성 X)
    public String saveDraft(ContractDto dto, int parentId, String parentSignature) {
        MemberEntity parent = memberRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("부모 정보를 찾을 수 없습니다."));

        // 부모 서명 이미지 저장
        String parentSignaturePath = signatureService.saveSignatureImage(parentSignature, "parent_" + parent.getMemberName());

        ContractEntity contract = ContractEntity.builder()
                .member(parent)
                .contractMoney(dto.getContract_money())
                .contractContent(dto.getContract_content())
                .contractStatus(false) // 초안 상태
                .contractDate(dto.getContract_date())
                .contractProvide(dto.getContract_provide())
                .contractImgpath(parentSignaturePath) // ✅ 부모 서명 이미지 경로 저장
                .build();

        contractRepository.save(contract);
        return "초안 저장 완료";
    }

    // ✅ 자녀가 서명 후 최종 계약서 생성 (PDF 생성)
    public String completeContract(int contractNum, String childSignature, int childId) {
        ContractEntity contract = contractRepository.findById(contractNum)
                .orElseThrow(() -> new RuntimeException("계약서를 찾을 수 없습니다."));

        // 자녀 서명 이미지 저장
        String childSignaturePath = signatureService.saveSignatureImage(childSignature, "child_" + childId);

        // ✅ PDF 생성
        String pdfPath = pdfService.createContractPdf(
                contract.getContractNum(),
                contract.getMember().getMemberName(),  // 자녀 이름
                contract.getContractContent(),
                contract.getContractImgpath(), // 부모 서명 (저장된 이미지 경로)
                childSignaturePath            // 자녀 서명 (저장된 이미지 경로)
        );

        contract.setContractFilepath(pdfPath);
        contract.setContractStatus(true); // 계약 활성화

        contractRepository.save(contract);
        return "계약이 최종 완료되었습니다.";
    }

    // ✅ 유효한 계약서 조회
    public Optional<ContractEntity> getValidContract(int childNum) {
        return contractRepository.findFirstByMember_MemberNumAndContractStatus(childNum, true);
    }

    // ✅ 계약서 비활성화
    public String disableContract(int contractNum) {
        Optional<ContractEntity> contractOpt = contractRepository.findById(contractNum);

        if (contractOpt.isPresent()) {
            ContractEntity contract = contractOpt.get();
            contract.setContractStatus(false);
            contractRepository.save(contract);
            return "계약서가 비활성화되었습니다.";
        }
        return "계약서를 찾을 수 없습니다.";
    }
}
