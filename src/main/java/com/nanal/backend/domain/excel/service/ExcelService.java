package com.nanal.backend.domain.excel.service;

import com.nanal.backend.domain.analysis.repository.DiaryLogRepository;
import com.nanal.backend.domain.excel.dto.resp.DauDto;
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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ExcelService {

    private final DiaryLogRepository diaryLogRepository;

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

}
