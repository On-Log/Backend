package com.nanal.backend.domain.auth.repository;

import com.nanal.backend.domain.analysis.dto.resp.DesignatedRetrospectDayDto;
import com.nanal.backend.domain.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findBySocialId(String socialId);

    @Query(value = "SELECT m.memberId FROM Member m WHERE m.retrospectDay = :dayOfWeek")
    List<Long> findMemberIdByRetrospectDay(@Param("dayOfWeek") DayOfWeek dayOfWeek);

    @Query(value = "SELECT new com.nanal.backend.domain.analysis.dto.resp.DesignatedRetrospectDayDto(m.retrospectDay, COUNT(m.email)) " +
            "FROM Member m " +
            "GROUP BY m.retrospectDay")
    List<DesignatedRetrospectDayDto> designatedRetrospectDayQuery();

}
