package com.shinhan.zoomoney.daily;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/daily")
@RequiredArgsConstructor
public class DailyController {

	private final DailyService dailyService;

	// 출석 여부 확인 api (DB 업데이트 X)
	@GetMapping("/status")
	public ResponseEntity<?> checkAttendanceStatus(@RequestParam("memberNum") int memberNum) {

		boolean isChecked = dailyService.isChecked(memberNum);
		return ResponseEntity.ok(Collections.singletonMap("isChecked", isChecked));
	}

	// 출석체크 api (DB 업데이트 O)
	@PostMapping("/check")
	public ResponseEntity<?> takeAttendance(@RequestParam("memberNum") int memberNum) {

		boolean success = dailyService.markAttendance(memberNum);
		return ResponseEntity.ok(Collections.singletonMap("success", success));
	}

}
