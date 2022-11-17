package com.nanal.backend.domain.retrospect.repository;

import com.nanal.backend.entity.RetrospectKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RetrospectKeywordRepository extends JpaRepository<RetrospectKeyword, Long> {
//    @Query(value = "SELECT * FROM retrospect re WHERE re.member_id = :memberId AND re.classify = :classify  AND re.writedate = :writedate ORDER BY re.write_date asc", nativeQuery = true)
//    List<RetrospectKeyword> findListByMemberAndClassifyAndWriteDate(Long memberId, String classify, LocalDateTime writedate);

    @Query(value = "SELECT * FROM retrospect_keyword re WHERE re.retrospect_id = :retrospectKeywordId AND re.classify LIKE :likeFormat ", nativeQuery = true)
    List<RetrospectKeyword> findListByRetroAndClassify(Long retrospectKeywordId, String likeFormat);

}
