package com.nanal.backend.domain.mypage.repository;

import com.nanal.backend.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Query(value = "SELECT m.memberId FROM Member m WHERE m.retrospectDay = :dayOfWeek")
    List<Long> findMemberIdByRetrospectDay(@Param("dayOfWeek") DayOfWeek dayOfWeek);
}
