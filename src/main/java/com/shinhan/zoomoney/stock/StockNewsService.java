package com.shinhan.zoomoney.stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StockNewsService {
	// application.properties 에서 키값 가져오기
	@Value("${naver.client.id}")
	private String clientId;
	@Value("${naver.client.secret}")
	private String clientSecret;

	public String searchNews(String query) {
		String text = encodedQuery(query);
		String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text;

		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("X-Naver-Client-Id", clientId);
		requestHeaders.put("X-Naver-Client-Secret", clientSecret);
		System.out.println(clientId + clientSecret);
		return get(apiURL, requestHeaders);
	}

	private String encodedQuery(String query) {
		try {
			return URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("검색어 인코딩 실패~", e);
		}
	}

	// url 연결 메소드
	private static String get(String apiURL, Map<String, String> requestHeaders) {
		// 해당 url을 HttpURLConnenciton 타입으로 연결
		HttpURLConnection con = connect(apiURL);
		try {
			// GET방식으로 연결
			con.setRequestMethod("GET");
			// 키값 세팅
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				return readBody(con.getInputStream());
			} else {
				return readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패~", e);
		} finally {
			con.disconnect();
		}

	}

	private static HttpURLConnection connect(String apiURL) {
		try {
			URL url = new URL(apiURL);
			return (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL 연결이 잘못되었습니다. >> " + apiURL, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. >>" + apiURL, e);
		}
	}

	private static String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);
		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();

			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}
			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
		}
	}

}

