package com.nanal.backend.domain.retrospect.dto.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RespCheckFirstRetrospect {
    //회고일 변경 후 첫 회고인지 첫 회고이면 true, 아니면 false
    Boolean firstRetrospect;
    Boolean writtenDiary;
    Integer diaryCount;

    static public RespCheckFirstRetrospect firstRetrospectAfterChange(boolean firstRetrospect, boolean writtenDiary, int diaryCount) {
        return RespCheckFirstRetrospect.builder()
                .firstRetrospect(firstRetrospect)
                .writtenDiary(writtenDiary)
                .diaryCount(diaryCount)
                .build();
    }

    static public RespCheckFirstRetrospect notFirstRetrospectAfterChange(boolean firstRetrospect, boolean writtenDiary, int diaryCount) {
        return RespCheckFirstRetrospect.builder()
                .firstRetrospect(firstRetrospect)
                .writtenDiary(writtenDiary)
                .diaryCount(diaryCount)
                .build();
    }



}
