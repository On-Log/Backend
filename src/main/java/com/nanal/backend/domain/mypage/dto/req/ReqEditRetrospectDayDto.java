package com.nanal.backend.domain.mypage.dto.req;

import com.nanal.backend.global.validation.ValueOfEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqEditRetrospectDayDto {
    @NotNull(message = "retrospectDay 는 비어있을 수 없습니다.")
    @ValueOfEnum(enumClass = DayOfWeek.class, message = "올바른 요일을 입력해주세요.")
    private String retrospectDay;
}
