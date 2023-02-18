package com.nanal.backend.domain.onboarding.service;

import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.global.exception.customexception.MemberAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;

@RequiredArgsConstructor
@Transactional
@Service
public class OnBoardingService {

    private final MemberRepository memberRepository;

    public void setRetrospectDay(String socialId, String retrospectDay) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        member.setRetrospectDay(DayOfWeek.valueOf(retrospectDay));
    }
}
