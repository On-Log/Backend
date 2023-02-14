package com.nanal.backend.domain.retrospect.dto.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespGetInfoDto {
    //유저 닉네임
    @NotNull(message = "nickName 값은 비어있을 수 없습니다.")
    String nickName;
    //회고 목적
    @NotBlank(message = "list는 비어있을 수 없습니다.")
    List<String> existRetrospect;

    //다음 회고까지 남은 날짜
    @NotBlank(message = "다음 회고까지 남은 날짜는 비어있을 수 없습니다.")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer betweenDate;


    @NotNull(message = "boolean 값은 비어있을 수 없습니다.")
    //회고 개수가 5개인지 아닌지 체크
    Boolean countRetrospect;

    //키워드 분류하고, 주차별로 나누기
    List<RespGetClassifiedKeywordDto> keywordList;

    public static RespGetInfoDto makeRespGetInfoDto(String nickName, List<String> existRetrospect, int betweenDate, boolean countRetrospect, List<RespGetClassifiedKeywordDto> respGetClassifiedKeywordDtos){
        RespGetInfoDto respGetInfoDto = RespGetInfoDto.builder()
                .nickName(nickName)
                .existRetrospect(existRetrospect)
                .betweenDate(betweenDate)
                .countRetrospect(countRetrospect)
                .keywordList(respGetClassifiedKeywordDtos)
                .build();

        return respGetInfoDto;
    }

}
