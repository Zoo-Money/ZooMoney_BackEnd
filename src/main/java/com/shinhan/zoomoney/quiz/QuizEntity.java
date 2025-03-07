package com.shinhan.zoomoney.quiz;

import java.util.Date;

import com.shinhan.zoomoney.member.MemberEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Quiz")
public class QuizEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int quizNum;

	@ManyToOne
	@JoinColumn(name = "member_num")
	private MemberEntity member;

	private boolean quizCheck;
	private Date quizDate;
}
