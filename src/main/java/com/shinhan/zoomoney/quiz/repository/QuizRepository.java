package com.shinhan.zoomoney.quiz.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shinhan.zoomoney.quiz.entity.QuizEntity;

public interface QuizRepository extends JpaRepository<QuizEntity, Integer> {

	// ✅ 사용자가 응시한 퀴즈 개수 구하기
	@Query("SELECT COUNT(q) FROM QuizEntity q WHERE q.member.memberNum = :memberNum AND DATE(q.quizDate) = CURRENT_DATE")
	int countQuiz(@Param("memberNum") int memberNum);

	// ✅ 하루 동안 맞춘 퀴즈의 개수 구하기
	@Query("SELECT COUNT(q) FROM QuizEntity q WHERE q.member.memberNum = :memberNum AND q.quizCheck = true AND DATE(q.quizDate) = CURRENT_DATE")
	int countCorrectAnswer(@Param("memberNum") int memberNum);

	// ✅ 오늘 날짜 기준으로 사용자가 푼 퀴즈의 정답 여부 리스트 가져오기
	// TIMESTAMP 타입의 quiz_date를 조회할 때, 날짜 범위로 검색해야함
	List<QuizEntity> findByMember_MemberNumAndQuizDateBetween(int memberNum, Timestamp start, Timestamp end);

}