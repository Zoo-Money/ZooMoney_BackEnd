package com.shinhan.zoomoney.quiz;


import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QuizService {
	private final QuizRepository quizRepository;
	private final RestTemplate restTemplate;
	
	public QuizService(QuizRepository quizRepository) {
		this.quizRepository = quizRepository;
		this.restTemplate = new RestTemplate();
	}
	
//	public List<Map<String, String>> getQuizFromAI() {
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		
//		String requestBody = "{ \"contents\": [{ \"parts\":[{ \"text\": " //
//				+ "\"금융 관련 OX 퀴즈를 랜덤으로 다섯 개 만들어줘. " // 
//				+ "문제와 답변은 서로 모두 달라야해. JSON 형식으로 한국어로 응답해줘. " //
//				+ "예제: { 'question': '질문 내용', 'answer': 'O 또는 X', 'explanation': '정답에 대한 간단한 설명' }\"}]}]}";
//	}
	
	public void parseQuizResponse() {
		
	}

}
