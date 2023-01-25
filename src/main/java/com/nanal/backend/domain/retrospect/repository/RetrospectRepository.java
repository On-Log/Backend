package com.nanal.backend.domain.retrospect.repository;

import com.nanal.backend.domain.retrospect.entity.Retrospect;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {

    @Query(value = "SELECT * FROM retrospect re WHERE re.member_id = :memberId AND re.write_date LIKE :likeFormat ORDER BY re.write_date asc", nativeQuery = true)
    List<Retrospect> findListByMemberAndWriteDate(Long memberId, String likeFormat);

//    @Query(value = "SELECT * FROM retrosepct re WHERE re.member_id = :memberId AND d.write_date LIKE :likeFormat", nativeQuery = true)
//    Diary findDiaryByMemberAndWriteDate(Long memberId, String likeFormat);
    @Modifying
    @Query(value = "UPDATE retrospect re SET re.edit_status = false WHERE re.member_id IN (:member_ids)", nativeQuery = true)
    void updateEditStatusByMember(@Param("member_ids") List<Long> member_ids);

    @Query(value = "SELECT * FROM retrospect re WHERE re.member_id = :memberId", nativeQuery = true)
    List<Retrospect> findListByMember(Long memberId);
}


