package com.shinhan.zoomoney.daily;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/daily")
@RequiredArgsConstructor
public class DailyController {

	private final DailyService dailyService;

	// 출석 여부 확인 api (DB 업데이트 X)
	@GetMapping("/status")
	public ResponseEntity<?> checkAttendanceStatus(HttpSession session) {
		Integer memberNum = (Integer) session.getAttribute("member_num");

		if (memberNum == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Collections.singletonMap("message", "로그인이 필요합니다."));
		}

		boolean isChecked = dailyService.isChecked(memberNum);
		return ResponseEntity.ok(Collections.singletonMap("isChecked", isChecked));
	}

	// 출석체크 api (DB 업데이트 O)
	@PostMapping("/check")
    public ResponseEntity<?> takeAttendance(HttpSession session) {
        Integer memberNum = (Integer) session.getAttribute("member_num");

        if (memberNum == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "로그인이 필요합니다."));
        }

        boolean success = dailyService.markAttendance(memberNum);
        return ResponseEntity.ok(Collections.singletonMap("success", success));
    }

}
