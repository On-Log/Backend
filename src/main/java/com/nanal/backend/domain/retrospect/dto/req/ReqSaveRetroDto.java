package com.nanal.backend.domain.retrospect.dto.req;

import com.nanal.backend.domain.retrospect.dto.RetrospectContentDto;
import com.nanal.backend.domain.retrospect.dto.RetrospectKeywordDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReqSaveRetroDto {

    private LocalDateTime currentDate;

    @NotBlank(message = "goal 은 비어있을 수 없습니다.")
    private String goal;

    @Valid
    private List<RetrospectContentDto> contents;

    @Valid
    private List<RetrospectKeywordDto> keywords;
}
