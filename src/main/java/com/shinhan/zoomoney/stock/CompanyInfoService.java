package com.shinhan.zoomoney.stock;

import org.springframework.stereotype.Service;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@Service
public class CompanyInfoService {
	public String getCompanyInfo(String stockId) {
		
		
		// WebDriverManager를 항상 사용하여 ChromeDriver 설정
		WebDriverManager.chromedriver().setup();
		
		// Chrome 옵션 설정 (headless 모드)
		ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--no-sandbox");

        WebDriver driver = new ChromeDriver(options);
		
		
        try {
            String url = "https://tossinvest.com/stocks/" + stockId + "/analytics?menu=profile";
            driver.get(url);
            // 페이지 로딩 대기
            Thread.sleep(5000); 

            WebElement companyInfo = driver.findElement(By.cssSelector(".css-1stmyb3"));
            // 크롤링한 기업 설명 반환
            return companyInfo.getText(); 
        } catch (Exception e) {
            e.printStackTrace();
            // 크롤링 실패 시 에러 메시지 반환
            return "Error Crawling"; 
        } finally {
            driver.quit();
        }
    }
		
	
	// 기존 경로 확인 함수
	private String getExistingChromeDriver() {
		String[] possiblePaths = {
				// macOS/Linux 기본 경로
				"/usr/local/bin/chromedriver",
				// Ubuntu 기본 경로
	            "/usr/bin/chromedriver",
	            // Windows 일반 설치 경로
	            "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe", 
	            // 일부 Windows 환경
	            "C:\\chromedriver.exe"  
		};
		
		for(String path:possiblePaths) {
			File file = new File(path);
			if(file.exists()) {
				System.out.println("기존 ChromeDriver 발견 : " + path);
				return path;
			}
		}
		System.out.println("기존 ChromeDriver 없음. WebDriverManager 사용.");
		return null;
	}
}
