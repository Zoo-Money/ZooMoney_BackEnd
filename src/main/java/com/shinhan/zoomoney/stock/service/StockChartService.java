package com.shinhan.zoomoney.stock.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.shinhan.zoomoney.stock.dto.StockDto;
import com.shinhan.zoomoney.stock.entity.StockEntity;
import com.shinhan.zoomoney.stock.repository.StockChartRepository;

import jakarta.transaction.Transactional;

@Service
public class StockChartService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final StockChartTokenService tokenService;
    private final StockChartRepository stockRepository;
    private final CompanyInfoService companyInfoService;

    @Value("${stock.api.key}")
    private String apiKey;

    @Value("${stock.api.secret}")
    private String apiSecret;

    // API URL (시가총액 기준 TOP 30개만 가져올 수 있음)
    private static final String apiUrl = "https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/ranking/market-cap";

    public StockChartService(StockChartTokenService tokenService, StockChartRepository stockRepository,
            CompanyInfoService companyInfoService) {
        this.tokenService = tokenService;
        this.stockRepository = stockRepository;
        this.companyInfoService = companyInfoService;
    }

    public List<Map<String, Object>> getTopStocks() {
        try {
            String accessToken = tokenService.getAccessToken();

            // HTTP 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json; charset=utf-8");
            headers.set("authorization", "Bearer " + accessToken);
            headers.set("appkey", apiKey);
            headers.set("appsecret", apiSecret);
            headers.set("tr_id", "FHPST01740000");
            headers.set("custtype", "P");

            // GET 요청에 사용할 query parameters 설정
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("fid_cond_mrkt_div_code", "J")
                    .queryParam("fid_cond_scr_div_code", "20174")
                    .queryParam("fid_div_cls_code", "0")
                    .queryParam("fid_input_iscd", "0000")
                    .queryParam("fid_trgt_cls_code", "0")
                    .queryParam("fid_trgt_exls_cls_code", "0")
                    .queryParam("fid_input_price_1", "")
                    .queryParam("fid_input_price_2", "")
                    .queryParam("fid_vol_cnt", "");

            String finalUrl = uriBuilder.toUriString();

            // Get 요청 보내기
            HttpEntity<Object> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(finalUrl, HttpMethod.GET, entity, Map.class);

            // 응답 데이터 파싱
            Map<String, Object> responseBody = response.getBody();

            // JSON 구조에 따라서 데이터 가져오기
            List<Map<String, Object>> stockList = null;
            if (responseBody != null && responseBody.containsKey("output")) {
                stockList = (List<Map<String, Object>>) responseBody.get("output");
            }

            return stockList;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of(Map.of("error", "Failed to fetch market cap data"));
        }

    }

    // 주식 리스트를 저장하는 메서드
    @Transactional
    public void saveStockList(List<StockDto> stockDtoList) {
        List<StockEntity> stockEntities = stockDtoList.stream()
                .map(StockDto::toEntity) // 변환
                .collect(Collectors.toList());

        stockRepository.saveAll(stockEntities); // DB에 저장
    }

    // DB에서 저장된 주식 데이터 "StockDto"로 변환하여 반환
    public List<StockDto> getStockList() {
        return stockRepository.findAll().stream()
                .map(stock -> StockDto.fromEntity(
                        stock,
                        stock.getStockInfo()))
                .collect(Collectors.toList());
    }

    // DB 종목 번호 기반으로 toss에서 크롤링
    public List<StockDto> getStockInfoCrwaling() {
        List<StockEntity> stocks = stockRepository.findAll();

        List<StockDto> updatedStockDtos = stocks.stream()
                .map(stock -> {
                    String stockInfo = companyInfoService.getCompanyInfo(stock.getStockId());
                    StockDto stockDto = StockDto.fromEntity(stock, stockInfo);
                    return stockDto;
                })
                .collect(Collectors.toList());

        // 크롤링한 데이터를 Entity로 변환 후 DB에 저장
        List<StockEntity> updatedStockEntities = updatedStockDtos.stream()
                .map(StockDto::toEntity)
                .collect(Collectors.toList());

        // DB 저장 전에 기존 데이터 삭제
        stockRepository.deleteAll();

        // DB에 저장
        stockRepository.saveAll(updatedStockEntities);
        return updatedStockDtos;

    }

    // 특정 종목 코드 (stockId)로 stockInfo 가져오기 (단, stock_num이 31~60 사이인 경우)
    public String getStockInfoById(String stockId) {
        return stockRepository.findStockInfoByStockId(stockId);
    }

    // Stock 테이블 갱신 함수
    @Transactional
    public List<StockDto> updateStockTable() {
        // 기존 데이터 삭제 (Stock 테이블 전체 삭제)
        stockRepository.deleteAll();

        // 새로운 데이터 API에서 가져오기
        List<Map<String, Object>> stockList = getTopStocks();

        List<StockDto> stockDtos = stockList.stream()
                .map(stock -> new StockDto(
                        null,
                        (String) stock.get("hts_kor_isnm"), // 주식 종목명
                        (String) stock.get("mksc_shrn_iscd"), // 주식 코드
                        null, // stock_info는 아직 없음
                        0 // stock_price는 아직 없음
                ))
                .collect(Collectors.toList());

        // 종목 코드 기반으로 크롤링하여 stockInfo 업데이트
        List<StockDto> updatedStockDtos = stockDtos.stream()
                .map(dto -> {
                    String stockInfo = companyInfoService.getCompanyInfo(dto.getStock_id());
                    dto.setStock_info(stockInfo);
                    return dto;
                })
                .collect(Collectors.toList());

        // 크롤링한 데이터를 Entity로 변환 후 DB에 저장
        List<StockEntity> updatedStockEntities = updatedStockDtos.stream()
                .map(StockDto::toEntity)
                .collect(Collectors.toList());
        // 새로운 데이터 저장
        stockRepository.saveAll(updatedStockEntities);
        return updatedStockDtos;
    }
    

}
