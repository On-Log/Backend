package com.nanal.backend.domain.retrospect.repository.retrospect;

import com.nanal.backend.domain.diary.dto.resp.RetrospectInfoDto;
import com.nanal.backend.domain.retrospect.entity.Retrospect;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RetrospectCustomRepository {

    void checkRetrospectAlreadyExist(Long memberId, LocalDateTime date);

    Boolean existDiaryDate(Long memberId, LocalDateTime date);

    Boolean checkRetroNotOverFive(Long memberId, LocalDateTime fromDate, LocalDateTime toDate);

    List<String> getRetrospectGoal(Long memberId, LocalDateTime fromDate, LocalDateTime toDate);

    List<RetrospectInfoDto> findRetrospectList(Long memberId, LocalDateTime fromDate, LocalDateTime toDate);

    Optional<Retrospect> findRetrospectByMemberAndWriteDate(Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    List<Retrospect> findRetrospectListByMemberAndWriteDate(Long memberId, LocalDateTime startDate, LocalDateTime endDate);
}
