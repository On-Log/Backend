package com.nanal.backend.domain.alarm.controller;

import com.nanal.backend.domain.alarm.dto.RequestDTO;
import com.nanal.backend.domain.alarm.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class AlarmController {

    private final AlarmService alarmService;

    @PostMapping("/api/fcm")
    public ResponseEntity pushMessage(@RequestBody RequestDTO requestDTO) throws IOException {
        alarmService.sendMessageTo(
                requestDTO.getTargetToken(),
                requestDTO.getTitle(),
                requestDTO.getBody());
        return ResponseEntity.ok().build();
    }
}
