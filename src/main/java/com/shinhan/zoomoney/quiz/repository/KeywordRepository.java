package com.shinhan.zoomoney.quiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shinhan.zoomoney.quiz.entity.KeywordEntity;

public interface KeywordRepository extends JpaRepository<KeywordEntity, Integer> {

	// 키워드 2개 뽑기
	@Query(value = "SELECT k.keyword_word FROM quiz_keyword k ORDER BY RAND() LIMIT 2", nativeQuery = true)
	List<String> findRandomKeywordWords();

}
