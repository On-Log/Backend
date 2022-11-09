package com.nanal.backend.domain.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespGetEmotionDto {

    private List<String> emotion;
}
