package com.nanal.backend.domain.analysis;

import com.nanal.backend.domain.analysis.dto.*;
import com.nanal.backend.entity.log.repository.AuthLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnalysisService {

    private final AuthLogRepository authLogRepository;

    public RespGetDauDto getDau() {
        LocalDateTime currentDate = LocalDate.now().atStartOfDay();

        LocalDateTime first = currentDate.withDayOfMonth(1);
        LocalDateTime last = currentDate.withDayOfMonth(1).plusMonths(1);

        List<DayDto> dayDtoList = authLogRepository.dauQuery(first, last);

        return RespGetDauDto.builder()
                .dayDtoList(dayDtoList)
                .build();
    }

    public RespGetWauDto getWau() {
        LocalDateTime currentDate = LocalDate.now().atStartOfDay();

        LocalDateTime first = currentDate.withDayOfMonth(1);
        LocalDateTime last = currentDate.withDayOfMonth(1).plusMonths(1);

        List<WeekDto> weekDtoList = authLogRepository.wauQuery(first, last);

        return RespGetWauDto.builder()
                .weekDtoList(weekDtoList)
                .build();
    }

    public RespGetMauDto getMau() {
        LocalDateTime currentDate = LocalDate.now().atStartOfDay();

        LocalDateTime first = currentDate.withDayOfMonth(1);
        LocalDateTime last = currentDate.withDayOfMonth(1).plusMonths(1);

        RespGetMauDto respGetMauDto = authLogRepository.mauQuery(first, last);
        return respGetMauDto;
    }


}
