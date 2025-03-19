package com.shinhan.zoomoney.contract;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfService {

	public String createContractPdf(int contractId, String childName, String contractContent,
			String parentSignaturePath, String childSignaturePath) {

		String fileName = String.format("contract_%d_%s_%s.pdf", contractId, childName.replaceAll("\\s", ""),
				LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

		String filePath = "src/main/resources/static/contract_pdf/" + fileName;

		// 폴더가 없으면 생성
		Path directoryPath = Paths.get("src/main/resources/static/contract_pdf/");
		if (!Files.exists(directoryPath)) {
			try {
				Files.createDirectories(directoryPath);
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}

		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(filePath));
			document.open();

			// 폰트
			BaseFont baseFont = BaseFont.createFont("src/main/resources/fonts/malgun.ttf", BaseFont.IDENTITY_H,
					BaseFont.EMBEDDED);
			Font titleFont = new Font(baseFont, 36, Font.BOLD);
			Font contentFont = new Font(baseFont, 24, Font.NORMAL);

			// "용돈 계약서" 제목을 가운데 정렬
			Paragraph title = new Paragraph("용돈 계약서", titleFont);
			title.setAlignment(Element.ALIGN_CENTER); // 제목 가운데 정렬 추가
			title.setSpacingAfter(30); // 제목과 본문 사이 간격 추가
			document.add(title);

			// 계약 내용 추가
			Paragraph content = new Paragraph("계약 내용: ", contentFont);
			content.setAlignment(Element.ALIGN_LEFT); // 본문은 좌측 정렬
			content.setSpacingAfter(30); // 본문과 다음 내용 간 간격 추가
			document.add(content);

			// 계약 내용을 줄바꿈 처리하여 추가
			String[] contentLines = contractContent.split("\n");
			for (String line : contentLines) {
				Paragraph lineParagraph = new Paragraph(line, contentFont);
				lineParagraph.setAlignment(Element.ALIGN_LEFT); // 좌측 정렬 유지
				document.add(lineParagraph);
			}

			// 추가 공백 (테이블과 계약 내용 사이 간격 추가)
			document.add(new Paragraph("\n\n"));

			// 부모 서명 & 자녀 서명을 한 줄에 추가 (테이블 사용)
			PdfPTable table = new PdfPTable(2); // 2열 테이블 생성
			table.setWidthPercentage(100); // 🔹 테이블 너비 설정

			PdfPCell parentCell = new PdfPCell();
			parentCell.addElement(new Paragraph("부모 서명:", contentFont));
			Image parentImage = Image.getInstance(parentSignaturePath);
			parentImage.scaleToFit(100, 50);
			parentCell.addElement(parentImage);
			// parentCell.setBorder(Rectangle.NO_BORDER); // 테두리 제거

			PdfPCell childCell = new PdfPCell();
			childCell.addElement(new Paragraph("자녀 서명:", contentFont));
			Image childImage = Image.getInstance(childSignaturePath);
			childImage.scaleToFit(100, 50);
			childCell.addElement(childImage);
			// childCell.setBorder(Rectangle.NO_BORDER); // 테두리 제거

			table.addCell(parentCell);
			table.addCell(childCell);

			document.add(table); // 최종적으로 테이블 추가

		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			document.close();
		}
		return fileName; // 파일명만 반환하도록 변경

	}
}
