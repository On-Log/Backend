package com.nanal.backend.domain.auth.repository;

import com.nanal.backend.domain.auth.entity.Member;

public interface MemberCustomRepository {

    Member findMember(String socialId);
}
