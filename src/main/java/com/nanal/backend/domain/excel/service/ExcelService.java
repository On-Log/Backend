package com.nanal.backend.domain.excel.service;

import com.nanal.backend.domain.analysis.entity.DiaryLog;
import com.nanal.backend.domain.analysis.repository.DiaryLogRepository;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.excel.dto.resp.DauDto;
import com.nanal.backend.domain.excel.dto.resp.UserDto;
import com.nanal.backend.domain.excel.dto.resp.WauDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ExcelService {

    private final DiaryLogRepository diaryLogRepository;
    private final MemberRepository memberRepository;
    private static final LocalDateTime FROM_DATE = LocalDateTime.of(2023, 4, 17, 0, 0, 0);
    private static final LocalDateTime TO_DATE = LocalDate.now().atTime(LocalTime.MAX);

    public byte[] getDauByExcel() {
        // 전체 기간 DAU 가져오기
        List<DauDto> dauDtoList = diaryLogRepository.excelDauQuery(FROM_DATE, TO_DATE);

        // 엑셀 파일 생성 및 데이터 작성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("DAU");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("날짜");
        headerRow.createCell(1).setCellValue("접속자 수");

        // 날짜별로 데이터 작성
        LocalDateTime currentDate = FROM_DATE;
        int rowIndex = 1;
        while (currentDate.isBefore(TO_DATE) || currentDate.isEqual(TO_DATE)) {
            Row row = sheet.createRow(rowIndex);
            DauDto dauDto = findDataByAccessDate(currentDate, dauDtoList);
            row.createCell(0).setCellValue(currentDate.toLocalDate().toString());
            row.createCell(1).setCellValue(dauDto != null ? dauDto.getUserCount() : 0);
            currentDate = currentDate.plusDays(1);
            rowIndex++;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 예외 처리 로직 추가
        }

        return outputStream.toByteArray();
    }

    public byte[] getWauByExcel() {
        // 전체 기간 WAU 가져오기
        List<WauDto> wauDtoList = getWauData(FROM_DATE, TO_DATE);

        // 엑셀 파일 생성 및 데이터 작성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("WAU");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("주차 시작일");
        headerRow.createCell(1).setCellValue("주차");
        headerRow.createCell(2).setCellValue("접속자 수");

        // 날짜별로 데이터 작성
        LocalDateTime currentDate = FROM_DATE;
        int rowIndex = 1;
        while (currentDate.isBefore(TO_DATE) || currentDate.isEqual(TO_DATE)) {
            Row row = sheet.createRow(rowIndex);
            WauDto dataForWeek = findDataByWeek(currentDate, wauDtoList);
            row.createCell(0).setCellValue(currentDate.toLocalDate().toString());
            row.createCell(1).setCellValue(rowIndex);
            row.createCell(2).setCellValue(dataForWeek != null ? dataForWeek.getUserCount() : 0);
            currentDate = currentDate.plusDays(7);
            rowIndex++;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 예외 처리 로직 추가
        }

        return outputStream.toByteArray();
    }

    public byte[] getJoinByExcel() {
        // 전체 기간 Join 가져오기
        List<UserDto> userDtoList = memberRepository.memberDauQuery(FROM_DATE, TO_DATE);

        // 엑셀 파일 생성 및 데이터 작성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("JOIN");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("날짜");
        headerRow.createCell(1).setCellValue("가입자 수");

        // 날짜별로 데이터 작성
        LocalDateTime currentDate = FROM_DATE;
        int rowIndex = 1;
        while (currentDate.isBefore(TO_DATE) || currentDate.isEqual(TO_DATE)) {
            Row row = sheet.createRow(rowIndex);
            UserDto userDto = findDataByCreateDate(currentDate, userDtoList);
            row.createCell(0).setCellValue(currentDate.toLocalDate().toString());
            row.createCell(1).setCellValue(userDto != null ? userDto.getUserCount() : 0);
            currentDate = currentDate.plusDays(1);
            rowIndex++;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 예외 처리 로직 추가
        }

        return outputStream.toByteArray();
    }




    //편의 메서드
    //WauDto 매핑
    public List<WauDto> getWauData(LocalDateTime from, LocalDateTime to) {
        List<DiaryLog> logs = diaryLogRepository.findLogsBetweenDates(from, to);

        Map<Integer, WauDto> weekCounts = new HashMap<>();
        for (DiaryLog log : logs) {
            Integer weekNumber = log.getCreatedAt().get(WeekFields.ISO.weekOfYear());
            Long count = weekCounts.getOrDefault(weekNumber, new WauDto(weekNumber, 0L, null)).getUserCount() + 1;
            LocalDateTime weekStartDate = log.getCreatedAt().with(WeekFields.ISO.dayOfWeek(), 1);
            weekCounts.put(weekNumber, new WauDto(weekNumber, count, weekStartDate));
        }

        return new ArrayList<>(weekCounts.values());
    }

    // 주차에 해당하는 데이터를 찾는 메서드
    private WauDto findDataByWeek(LocalDateTime date, List<WauDto> weekDtoList) {
        LocalDate searchDate = date.toLocalDate();
        for (WauDto dto : weekDtoList) {
            if (dto.getWeekStartDate().toLocalDate().equals(searchDate)) {
                return dto;
            }
        }
        return null;
    }

    // 날짜에 해당하는 데이터를 찾는 메서드 (접속자)
    private DauDto findDataByAccessDate(LocalDateTime date, List<DauDto> dayDtoList) {
        LocalDate searchDate = date.toLocalDate();
        for (DauDto dto : dayDtoList) {
            if (dto.getAccessTime().toLocalDate().equals(searchDate)) {
                return dto;
            }
        }
        return null;
    }

    // 날짜에 해당하는 데이터를 찾는 메서드 (가입자)
    private UserDto findDataByCreateDate(LocalDateTime date, List<UserDto> userDtoList) {
        LocalDate searchDate = date.toLocalDate();
        for (UserDto dto : userDtoList) {
            if (dto.getCreateDate().toLocalDate().equals(searchDate)) {
                return dto;
            }
        }
        return null;
    }

}
