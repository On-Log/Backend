package com.nanal.backend.domain.retrospect.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespGetInfoDto {
    //회고 목적
    List<String> existRetrospect
            ;
    //다음 회고까지 남은 시간
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer betweenDate;

    //키워드 분류하고, 주차별로 나누기
    List<List<List<String>>> existRetrospectKeyword;

}
