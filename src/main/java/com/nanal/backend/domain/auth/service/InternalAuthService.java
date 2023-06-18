package com.nanal.backend.domain.auth.service;

import com.nanal.backend.domain.alarm.entity.Alarm;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.event.RegisterEvent;
import com.nanal.backend.domain.auth.exception.AccountAlreadyExistException;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.global.lock.DistributedLock;
import com.nanal.backend.global.lock.LockName;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class InternalAuthService {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    @DistributedLock
    public Member auth(Member member, @LockName String email, String providerInfo) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if(isNewMember(findMember))
            return joinMembership(member);
        else
            return login(findMember, providerInfo);
    }

    private Member joinMembership(Member member) {
        Member newMember = register(member);
        publisher.publishEvent(new RegisterEvent(newMember.getNickname(), newMember.getEmail()));
        return newMember;
    }

    private static boolean isNewMember(Optional<Member> findMember) {
        return findMember.isEmpty();
    }

    private Member register(Member member) {
        return memberRepository.save(member);
    }

    private Member login(Optional<Member> member, String providerInfo) {
        Member loginMember = member.get();
        if(!providerInfo.contains((loginMember.getProvider().name().toLowerCase())))
            throw AccountAlreadyExistException.EXCEPTION;

        if(loginMember.getAlarm() == null) {
            loginMember.setAlarm(Alarm.createAlarm(loginMember));
        }
        return loginMember;
    }
}
