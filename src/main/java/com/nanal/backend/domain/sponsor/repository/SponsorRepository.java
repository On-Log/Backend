package com.nanal.backend.domain.sponsor.repository;

import com.nanal.backend.domain.sponsor.entity.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SponsorRepository extends JpaRepository<Sponsor, Long> {

    Optional<Sponsor> findByCode(String code);

}
