package com.nanal.backend.domain.mypage.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqWithdrawMembership {

    @Valid
    @Size(min = 1, max = 4, message ="reason의 개수는 최소 1개, 최대 4개입니다.")
    private List<Reason> reasons;

    @NotBlank(message = "detail는 비어있을 수 없습니다.")
    @Size(max = 500, message="detail은 최대 500개의 문자만 입력 가능합니다.")
    private String detail;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static public class Reason {
        @NotBlank(message = "content는 비어있을 수 없습니다.")
        @Size(max = 50, message="content는 최대 50개의 문자만 입력 가능합니다.")
        private String content;
    }
}

