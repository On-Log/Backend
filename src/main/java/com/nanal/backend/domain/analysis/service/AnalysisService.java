package com.nanal.backend.domain.analysis.service;

import com.nanal.backend.domain.analysis.dto.resp.*;
import com.nanal.backend.domain.analysis.repository.AuthLogRepository;
import com.nanal.backend.domain.analysis.repository.RetrospectLogRepository;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AnalysisService {

    private final AuthLogRepository authLogRepository;
    private final RetrospectLogRepository retrospectLogRepository;
    private final MemberRepository memberRepository;

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

    public RespGetWeekDayRetrospectDto getWeekDayRetrospect() {
        LocalDateTime currentDate = LocalDate.now().atStartOfDay();

        LocalDateTime first = currentDate.minusWeeks(1);
        LocalDateTime last = currentDate.minusDays(1);

        List<WeekDayDao> weekDayDaoList = retrospectLogRepository.weekDayRetrospectQuery("getInfo" ,first, last);
        List<WeekDayDto> weekDayDtoList = new ArrayList<>();
        for (WeekDayDao weekDayDao : weekDayDaoList) {
            WeekDayDto weekDayDto = new WeekDayDto();
            weekDayDto.setUserCount(weekDayDao.getUserCount());
            switch (weekDayDao.getDay()) {
                case 1 : weekDayDto.setDay("?????????");  break;
                case 2 : weekDayDto.setDay("?????????");  break;
                case 3 : weekDayDto.setDay("?????????");  break;
                case 4 : weekDayDto.setDay("?????????");  break;
                case 5 : weekDayDto.setDay("?????????");  break;
                case 6 : weekDayDto.setDay("?????????");  break;
                case 7 : weekDayDto.setDay("?????????");  break;
            }
        }

        return RespGetWeekDayRetrospectDto.builder()
                .weekDayDtoList(weekDayDtoList)
                .build();
    }


    public RespGetDesignatedRetrospectDayDto getDesignatedRetrospectDay() {

        List<DesignatedRetrospectDayDto> designatedRetrospectDayDtoList = memberRepository.designatedRetrospectDayQuery();

        return RespGetDesignatedRetrospectDayDto.builder()
                .designatedRetrospectDayDtoList(designatedRetrospectDayDtoList)
                .build();
    }
}
