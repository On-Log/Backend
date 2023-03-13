package com.nanal.backend.domain.retrospect.repository.retrospect;

import com.nanal.backend.domain.retrospect.entity.QRetrospect;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RetrospectCustomRepositoryImpl implements RetrospectCustomRepository{

    private final JPAQueryFactory queryFactory;

    QRetrospect retrospect = QRetrospect.retrospect;


}
