package com.nanal.backend.domain.retrospect.dto.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespGetInfoDto {
    //회고 목적
    @NotBlank(message = "list는 비어있을 수 없습니다.")
    List<String> existRetrospect;

    //다음 회고까지 남은 날짜
    @NotBlank(message = "다음 회고까지 남은 날짜는 비어있을 수 없습니다.")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer betweenDate;

    //키워드 분류하고, 주차별로 나누기
    List<RespGetClassifiedKeywordDto> keywordList;

    public static RespGetInfoDto makeRespGetInfoDto(List<String> existRetrospect, int betweenDate, List<RespGetClassifiedKeywordDto> respGetClassifiedKeywordDtos){
        RespGetInfoDto respGetInfoDto = RespGetInfoDto.builder()
                .existRetrospect(existRetrospect)
                .betweenDate(betweenDate)
                .keywordList(respGetClassifiedKeywordDtos)
                .build();

        return respGetInfoDto;
    }

}
