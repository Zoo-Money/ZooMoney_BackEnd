package com.shinhan.zoomoney.contract;


import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image; 
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfService {
	
//	//pdf 생성 메서드
//	public String createContractPdf(int contractId, String childName, String contractContent, String parentSignature, String childSignature) {
//		
//		// 파일명에 계약서 id + 자녀이름 + 생성날짜 포함
//		String fileName = String.format("contract_%d_%s_%s.pdf", contractId, childName.replaceAll("\\s",""), //공백제거
//				LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
//				);
//		
//		// 파일 저장 경로 설정!!!!!!??????????????????????????????????????????
//		String filePath = "C:/contract_pdfs" + fileName;
//		
//		// PDF 생성 로직
//		Document document = new Document();
//		try {
//			PdfWriter.getInstance(document, new FileOutputStream(filePath));
//			document.open();
//			
//			//계약서 제목 추가
//			document.add(new Paragraph("📝 용돈계약서", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
//			document.add(new Paragraph(" "));// 빈줄추가
//			
//			//계약서 내용 추가
//			document.add(new Paragraph("계약내용 : " + contractContent ));
//			document.add(new Paragraph("계약금액 : " + contractContent )); // 필요에 따라 수정가능
//			document.add(new Paragraph("지급 방법: 현금 지급")); // 예시 추가
//	        document.add(new Paragraph("계약 날짜: " + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
//	        
//	        document.add(new Paragraph(" "));
//            document.add(new Paragraph("👨‍👩‍👧‍👦 부모 서명: " + parentSignature));
//            document.add(new Paragraph("👦 자녀 서명: " + childSignature));
//			
//		}
//		catch(DocumentException | IOException e) {
//		     e.printStackTrace();
//	            return null;
//		}
//		finally {document.close();}
//				
//				
//				
//		return filePath; // 생성된 PDF 파일 경로 반환
//	}


    public String createContractPdf(int contractId, String childName, String contractContent, 
                                    String parentSignaturePath, String childSignaturePath) {

        String fileName = String.format("contract_%d_%s_%s.pdf",
            contractId,
            childName.replaceAll("\\s", ""),
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        );

        String filePath = "C:/contract_pdfs/" + fileName;

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("용돈 계약서"));
            document.add(new Paragraph("계약 내용: " + contractContent));

            // 부모 서명 이미지 추가
            Image parentImage = Image.getInstance(parentSignaturePath);
            parentImage.scaleToFit(150, 75);
            document.add(new Paragraph("부모 서명:"));
            document.add(parentImage);

            // 자녀 서명 이미지 추가
            Image childImage = Image.getInstance(childSignaturePath);
            childImage.scaleToFit(150, 75);
            document.add(new Paragraph("자녀 서명:"));
            document.add(childImage);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            document.close();
        }

        return filePath;
    }
}
