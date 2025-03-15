package com.shinhan.zoomoney.daily;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/daily")
@RequiredArgsConstructor
public class DailyController {
	
	private final DailyService dailyService;
	
	// 출석체크 api
	@PostMapping("/check")
	public ResponseEntity<?> takeAttendance(){
		
		boolean isChecked = dailyService.isChecked();
		
		// ✅ 프론트에서 isChecked를 받아 사용할 수 있도록 JSON 형태로 반환
	    return ResponseEntity.ok(Collections.singletonMap("isChecked", isChecked));
		
	}
	
}
