package com.shinhan.zoomoney.contract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class SignatureService {

    // 서명 이미지를 로컬에 저장하는 메서드
    public String saveSignatureImage(String base64Signature, String fileName) {
        try {
            String base64Data = base64Signature.split(",")[1];
            byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

            // 서명 이미지 경로 설정
            String filePath = "C:/signature_images/" + fileName + ".png";

            // 디렉토리 자동 생성 (경로 없을 시)
            File dir = new File("C:/signature_images/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 파일 저장
            File file = new File(filePath);
            try (OutputStream os = new FileOutputStream(file)) {
                os.write(decodedBytes);
            }

            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("서명 이미지 저장 실패");
        }
    }
}
