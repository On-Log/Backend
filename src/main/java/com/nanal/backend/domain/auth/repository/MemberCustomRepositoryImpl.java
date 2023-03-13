package com.nanal.backend.domain.auth.repository;

import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.entity.QMember;
import com.nanal.backend.global.exception.customexception.MemberAuthException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository{

    private final JPAQueryFactory queryFactory;

    QMember member = QMember.member;


    @Override
    public Member findMember(String socialId) {
        return queryFactory
                .selectFrom(member)
                .where(member.socialId.eq(socialId))
                .stream().findAny()
                .orElseThrow(() -> MemberAuthException.EXCEPTION);
    }
}
