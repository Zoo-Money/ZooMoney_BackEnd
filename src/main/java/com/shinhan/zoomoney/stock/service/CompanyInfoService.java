package com.shinhan.zoomoney.stock.service;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import io.github.bonigarcia.wdm.WebDriverManager;

@Service
public class CompanyInfoService {

    public String getCompanyInfo(String stockCode) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // UI 없이 실행
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(options);
        try {
            // Toss증권 페이지 URL
            String url = "https://tossinvest.com/stocks/" + stockCode + "/analytics?menu=profile";
            driver.get(url);

            // JavaScriptExecutor를 사용하여 페이지가 완전히 로드될 때까지 대기
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30), Duration.ofSeconds(1));
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));

            // 새 XPath 사용하여 요소 찾기
            WebElement companyInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, '_1n4jthd0')]//span[contains(@class, 'tw-1r5dc8g0')]")));

            // 크롤링 결과 출력
            String companyDescription = companyInfo.getText();

            return companyDescription;

        } catch (Exception e) {
            e.printStackTrace();
            return "크롤링 실패";
        } finally {
            // 실행 종료
            driver.quit();
        }
    }
}
