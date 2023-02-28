package com.nanal.backend.domain.retrospect.dto.req;

import com.nanal.backend.domain.retrospect.dto.resp.RetrospectContentDto;
import com.nanal.backend.domain.retrospect.dto.resp.RetrospectKeywordDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReqSaveRetroDto {

    @NotBlank(message = "goal 은 비어있을 수 없습니다.")
    private String goal;

    @Valid
    private List<RetrospectContentDto> contents;

    @Valid
    private List<RetrospectKeywordDto> keywords;
}
