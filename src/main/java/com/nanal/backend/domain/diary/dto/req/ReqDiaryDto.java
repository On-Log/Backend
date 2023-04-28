package com.nanal.backend.domain.diary.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqDiaryDto {

    @PastOrPresent(message = "date 는 현재 또는 과거의 날짜만 가능합니다.")
    @NotNull(message = "date 값이 올바르지 않습니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    @NotBlank(message = "content 는 비어있을 수 없습니다.")
    @Size(min = 1, max = 600, message="content 는 최대 600개의 문자만 입력 가능합니다.")
    private String content;

    @Valid
    @Size(min = 1, max = 5, message ="keyword 의 개수는 최소 1개, 최대 5개입니다.")
    private List<KeywordDto> keywords;

    // 네이밍으로 인해 json parsing 대상. 어노테이션 제거시 Test 실패
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<String> getEmotions() {
        return this.keywords.stream()
                .flatMap(keywordDto -> keywordDto.getKeywordEmotions().stream())
                .map(keywordEmotionDto -> keywordEmotionDto.getEmotion())
                .distinct()
                .collect(Collectors.toList());
    }
}
