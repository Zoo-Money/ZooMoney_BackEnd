package com.shinhan.zoomoney.quiz.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.zoomoney.member.MemberEntity;
import com.shinhan.zoomoney.member.MemberRepository;
import com.shinhan.zoomoney.quiz.dto.QuizResponseDto;
import com.shinhan.zoomoney.quiz.dto.QuizSubmitDto;
import com.shinhan.zoomoney.quiz.entity.QuizEntity;
import com.shinhan.zoomoney.quiz.repository.KeywordRepository;
import com.shinhan.zoomoney.quiz.repository.QuizRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizService {

	private final RestTemplate restTemplate; // AI API 호출
	private final QuizRepository quizRepository;
	private final MemberRepository memberRepository;
	private final KeywordRepository keywordRepository; // KeywordRepository 주입

	 @Value("${gemini.api.key}")
	    private String geminiApiKey;

	    private static final String GEMINI_API_URL_BASE = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

	    public String getGeminiApiUrl() {
	        return GEMINI_API_URL_BASE + geminiApiKey;
	    }

	// ✅ AI에게 퀴즈 생성 요청 (DB에 저장 X)
	public QuizResponseDto generateFinancialQuiz() {

		// ✅ 랜덤 키워드 2개 가져오기
		List<String> keywords = keywordRepository.findRandomKeywordWords();

		String keyword1 = keywords.get(0);
		String keyword2 = keywords.get(1);

		String prompt = String.format(
				"'%s'와 '%s' 이 키워드와 관련된 새로운 금융 OX 퀴즈 한 문제를 만들어줘. 문제 자체에 (O/X)는 포함시킬 필요없어. 금융 지식이 부족한 사람도 풀 수 있을 수준의 문제여야해. JSON 형식으로 한국어로 응답해줘. 예제: { 'question': '질문 내용', 'answer': 'O 또는 X', 'explanation': '정답에 대한 간단한 설명' }",
				keyword1, keyword2);

		String quizJson = callGeminiApi(prompt);

		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(quizJson, QuizResponseDto.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String callGeminiApi(String prompt) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");

			// ✅ JSON 내부 문자열을 올바르게 구성하도록 수정
			String requestBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + prompt + "\" }] }],"
					+ " \"generationConfig\": { \"temperature\": 2.0, \"top_k\": 40, \"top_p\": 0.8 } }";

			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.exchange(getGeminiApiUrl(), HttpMethod.POST, entity,
					String.class);

			// JSON이 아니라면 예외 발생 가능 → 예외 처리 추가
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response.getBody());

			// ✅ JSON 내부에서 퀴즈 데이터가 있는 부분을 찾기
			String rawText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

			// ✅ 불필요한 ```json\n 및 ``` 제거
			String cleanedJson = rawText.replaceAll("```json\\n", "").replaceAll("```", "").trim();

			// ✅ 최종 JSON을 다시 파싱
			JsonNode quizArray = mapper.readTree(cleanedJson);

			return quizArray.toString(); // JSON 문자열 반환

		} catch (Exception e) {
			e.printStackTrace();
			return "퀴즈 생성 중 오류 발생";
		}
	}

	// ✅ 푼 퀴즈 data 개수 확인
	public Integer howManyQuiz(int memberNum) {
		int todayQuizCount = quizRepository.countQuiz(memberNum);
		return todayQuizCount;
	}

	// ✅ 맞춘 퀴즈 data 개수 확인
	public Integer howManyCorrectAnswer(int memberNum) {
		int correctAnswerCount = quizRepository.countCorrectAnswer(memberNum);
		return correctAnswerCount;
	}

	// ✅ 퀴즈 제출 및 DB 저장
	@Transactional
	public synchronized boolean submitQuiz(QuizSubmitDto quizSubmitDto, int memberNum) {
		boolean isCorrect = quizSubmitDto.getCorrectAnswer().equalsIgnoreCase(quizSubmitDto.getUserAnswer());
		
	    // 오늘 푼 퀴즈 개수 확인
	    int todayQuizCount = quizRepository.countQuiz(memberNum);

	    // 5개 이상이면 저장하지 않음
	    if (todayQuizCount >= 5) {
	        return false;
	    }
	    
		// ✅ 사용자 정보 조회
		MemberEntity member = memberRepository.findById(memberNum)
				.orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

		// ✅ 정답 맞추면 memberPoint 100점 추가 (null 값 처리 포함)
		if (isCorrect) {
			int currentPoint = (member.getMemberPoint() == null) ? 0 : member.getMemberPoint();
			member.setMemberPoint(currentPoint + 100);
			memberRepository.save(member);
		}

		// ✅ 퀴즈 결과 저장
		QuizEntity quizEntity = QuizEntity.builder().member(member).quizCheck(isCorrect).quizDate(new Date()).build();

		quizRepository.save(quizEntity);

		return isCorrect;
	}

	// ✅ 오늘 푼 퀴즈 정답 여부 리스트 가져오기
	public List<Integer> todayAnswerList(int memberNum) {
		// ✅ 오늘 날짜의 00:00:00 ~ 23:59:59 범위를 지정
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay(); // 2025-03-17 00:00:00
		LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59); // 2025-03-17 23:59:59

		Timestamp startTimestamp = Timestamp.valueOf(startOfDay);
		Timestamp endTimestamp = Timestamp.valueOf(endOfDay);

		// ✅ 사용자가 푼 퀴즈 리스트 조회 (quizCheck 값만 가져옴)
		List<QuizEntity> quizList = quizRepository.findByMember_MemberNumAndQuizDateBetween(memberNum, startTimestamp,
				endTimestamp);

		// ✅ quizCheck 값을 Integer(0, 1)로 변환하여 반환
		return quizList.stream().map(q -> q.isQuizCheck() ? 1 : 0).collect(Collectors.toList());
	}

}
