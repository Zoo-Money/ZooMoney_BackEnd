package com.shinhan.zoomoney.daily;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.shinhan.zoomoney.member.MemberEntity;
import com.shinhan.zoomoney.member.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DailyService {

	private final DailyRepository dailyRepository;
	private final MemberRepository memberRepository;

	// 출석 여부 확인 (조회 전용)
	public boolean isChecked(Integer memberNum) {

		// ✅ 테스트용 memberNum 고정
		// memberNum = 8;

		// ✅ 사용자 정보 조회
		MemberEntity member = memberRepository.findById(memberNum)
				.orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

		// ✅ 오늘 날짜의 출석체크 여부 확인
		LocalDate today = LocalDate.now();

		return dailyRepository.existsByMemberAndDailyDate(member, Date.valueOf(today));
	}

	// 출석 체크 (DB 업데이트)
	public boolean markAttendance(Integer memberNum) {

		// ✅ 사용자 정보 조회
		MemberEntity member = memberRepository.findById(memberNum)
				.orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

		// ✅ 오늘 날짜의 출석체크 여부 확인
		LocalDate today = LocalDate.now();
		boolean alreadyChecked = dailyRepository.existsByMemberAndDailyDate(member, Date.valueOf(today));

		if (alreadyChecked) {
			return false; // 이미 출석한 경우
		}

		// ✅ 출석체크 데이터 추가
		DailyEntity dailyentity = DailyEntity.builder()
				.member(member)
				.dailyCheck(true)
				.dailyDate(Date.valueOf(today))
				.build();
		dailyRepository.save(dailyentity);

		// ✅ point 10 증가
		member.setMemberPoint(member.getMemberPoint() + 10);
		memberRepository.save(member);

		return true;
	}

}
