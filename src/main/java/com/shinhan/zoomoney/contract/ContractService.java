package com.shinhan.zoomoney.contract;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shinhan.zoomoney.card.entity.CardEntity;
import com.shinhan.zoomoney.card.repository.CardRepository;
import com.shinhan.zoomoney.member.MemberEntity;
import com.shinhan.zoomoney.member.MemberRepository;

@Service
public class ContractService {

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private CardRepository cardRepository;

	@Autowired
	private PdfService pdfService;

	@Autowired
	private SignatureService signatureService;

	// 부모가 서명 후 초안 저장 (PDF 생성 X)
	public String saveDraft(ContractDto dto, int memberNum, String parentSignature) {
		// 부모정보 조회
		MemberEntity parent = memberRepository.findById(memberNum)
				.orElseThrow(() -> new RuntimeException("부모 정보를 찾을 수 없습니다."));

		// 부모 서명 이미지 저장
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String parentSignaturePath = signatureService.saveSignatureImage(parentSignature,
				"parent_" + parent.getMemberName() + "_" + today);
		MemberEntity child = memberRepository.findById(dto.getChild_num())
				.orElseThrow(() -> new RuntimeException("아이 정보를 찾을 수 없습니다."));

		ContractEntity contract = ContractEntity.builder().member(child).contractMoney(dto.getContract_money())
				.contractContent(dto.getContract_content()).contractStatus(false) // 초안 상태
				.contractDate(dto.getContract_date()).contractProvide(dto.getContract_provide())
				.contractImgpath(parentSignaturePath) // 부모 서명 이미지 경로 저장
				.build();

		contractRepository.save(contract);
		return "초안 저장 완료";
	}

	// 자녀가 서명 후 최종 계약서 생성 (PDF 생성)
	public String completeContract(int childNum, String childSignature) {
		// System.out.println(" 전달받은 childNum: " + childNum);

		ContractEntity contract = contractRepository
				.findFirstByMember_MemberNumAndContractStatusOrderByContractNumDesc(childNum, false)
				.orElseThrow(() -> new RuntimeException("계약서를 찾을 수 없습니다."));

		// 자녀정보 조회
		MemberEntity child = memberRepository.findById(childNum)
				.orElseThrow(() -> new RuntimeException("아이 정보를 찾을 수 없습니다."));

		// 자녀 서명 이미지 저장
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String childSignaturePath = signatureService.saveSignatureImage(childSignature,
				"child_" + child.getMemberName() + "_" + today);

		// PDF 생성 (부모 서명 + 자녀 서명 포함)
		String fileName = pdfService.createContractPdf(contract.getContractNum(), contract.getMember().getMemberName(),
				contract.getContractContent(), contract.getContractImgpath(), childSignaturePath);

		///////////// 03.16 21:34
		// API 경로로 변경 (프론트엔드에서 접근 가능하도록 설정)
		String filePath = "/contract_pdf/" + fileName;

		// DB 업데이트 (PDF 경로 및 계약 상태)
		contract.setContractFilepath(filePath); // PDF 파일 경로 저장
		contract.setContractStatus(true); // 최종 계약 완료 상태
		contract.setContractExcelpath(childSignaturePath); // 자녀 서명 이미지 경로 저장
		contractRepository.save(contract);

		// System.out.println("@@@@@@@@" + filePath);
		// contract.setContractFilepath(filePath); // ✅ 프론트엔드에서 접근할 수 있는 경로 저장

		return "계약이 최종 완료되었습니다.";
	}

	// 용돈 송금하기
	public void sendAllowance(int memberNum, int amount) {

		MemberEntity member = memberRepository.findById(memberNum)
				.orElseThrow(() -> new IllegalArgumentException("해당 자녀를 찾을 수 없습니다."));

		CardEntity card = cardRepository.findByMember(member);

		if (card == null) {
			throw new IllegalArgumentException("해당 자녀의 카드 정보가 없습니다.");
		}

		card.setCardMoney(card.getCardMoney() + amount);
		cardRepository.save(card);
	}

	// 유효한 계약서 조회
	public Optional<ContractEntity> getValidContract(int childNum) {
		return contractRepository.findFirstByMember_MemberNumAndContractStatus(childNum, true);
	}

	// 과거계약서 조회
	public List<ContractEntity> getPastContracts(int memberNum) {
		Pageable pageable = PageRequest.of(0, 6); // 0번째 페이지부터 6개만 조회
		Page<ContractEntity> contractsPage = contractRepository.findByMember_MemberNumOrderByContractNumDesc(memberNum,
				pageable);

		return contractsPage.getContent(); // Page 객체에서 데이터만 반환
	}

	// 부모 이름조회
	public String getParentName(int memberNum) {
		return memberRepository.findById(memberNum).map(MemberEntity::getMemberName) // 부모이름반환
				.orElseThrow(() -> new IllegalArgumentException("부모 정보를 찾을 수 없습니다."));
	}

	// 아이 이름조회
	public String getChildName(int memberNum) {
		return memberRepository.findById(memberNum).map(MemberEntity::getMemberName) // 아이이름반환
				.orElseThrow(() -> new IllegalArgumentException("아이 정보를 찾을 수 없습니다."));
	}
}
