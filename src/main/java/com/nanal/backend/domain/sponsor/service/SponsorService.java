package com.nanal.backend.domain.sponsor.service;

import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.sponsor.dto.req.ReqCheckSponsorDto;
import com.nanal.backend.domain.sponsor.entity.Sponsor;
import com.nanal.backend.domain.sponsor.exception.CodeAlreadyUsedException;
import com.nanal.backend.domain.sponsor.exception.CodeNotExistException;
import com.nanal.backend.domain.sponsor.repository.SponsorRepository;
import com.nanal.backend.global.exception.customexception.MemberAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class SponsorService {

    private final MemberRepository memberRepository;
    private final SponsorRepository sponsorRepository;

    @Transactional
    public void checkSponsor(String socialId, ReqCheckSponsorDto reqCheckSponsorDto) {
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);
        Sponsor sponsor = sponsorRepository.findByCode(reqCheckSponsorDto.getCode()).orElseThrow(() -> CodeNotExistException.EXCEPTION);
        checkCodeAlreadyUsed(sponsor);
        confirmSponsor(member, sponsor);
    }

    public void confirmSponsor(Member member, Sponsor sponsor) {
        member.setGoods(sponsor.getGoods().toString());
        sponsor.changeisConfirmed(true);
    }

    public void checkCodeAlreadyUsed(Sponsor sponsor) {
        if(sponsor.getIsConfirmed() == true) {
            throw CodeAlreadyUsedException.EXCEPTION;
        }
    }


}
