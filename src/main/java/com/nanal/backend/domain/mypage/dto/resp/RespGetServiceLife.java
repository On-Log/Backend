package com.nanal.backend.domain.mypage.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespGetServiceLife {

    private Integer serviceLife;
}
