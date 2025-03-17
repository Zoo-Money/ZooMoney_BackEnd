package com.shinhan.zoomoney.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuizRepository extends JpaRepository<QuizEntity, Integer> {
	
	// 사용자가 응시한 퀴즈 개수 구하기
	@Query("SELECT COUNT(q) FROM QuizEntity q WHERE q.member.memberNum = :memberNum AND DATE(q.quizDate) = CURRENT_DATE")
	int countQuiz(@Param("memberNum") int memberNum);
	
	// 하루 동안 맞춘 퀴즈의 개수 구하기
	@Query("SELECT COUNT(q) FROM QuizEntity q WHERE q.member.memberNum = :memberNum AND q.quizCheck = true AND DATE(q.quizDate) = CURRENT_DATE")
	int countCorrectAnswer(@Param("memberNum") int memberNum);

	
}