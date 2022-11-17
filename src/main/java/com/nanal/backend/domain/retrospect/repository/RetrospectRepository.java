package com.nanal.backend.domain.retrospect.repository;

import com.nanal.backend.entity.Retrospect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {

    @Query(value = "SELECT * FROM retrospect re WHERE re.member_id = :memberId AND re.write_date LIKE :likeFormat ORDER BY re.write_date asc", nativeQuery = true)
    List<Retrospect> findListByMemberAndWriteDate(Long memberId, String likeFormat);

//    @Query(value = "SELECT * FROM retrosepct re WHERE re.member_id = :memberId AND d.write_date LIKE :likeFormat", nativeQuery = true)
//    Diary findDiaryByMemberAndWriteDate(Long memberId, String likeFormat);
}
