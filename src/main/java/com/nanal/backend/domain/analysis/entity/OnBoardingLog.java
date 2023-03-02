package com.nanal.backend.domain.analysis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OnBoardingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "onBoarding_log_id")
    private Long onBoardingLogId;

    @Column(name = "user_email", nullable = false, length = 50)
    private String userEmail;

    @Column(name = "service_name", nullable = false, length = 30)
    private String serviceName;

    @Column(name = "execution_time", nullable = false)
    private Long executionTime;
}
