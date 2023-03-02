package com.nanal.backend.domain.analysis.entity;

import com.nanal.backend.global.config.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MypageLog extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mypage_log_id")
    private Long mypageLogId;

    @Column(name = "user_email", nullable = false, length = 50)
    private String userEmail;

    @Column(name = "service_name", nullable = false, length = 30)
    private String serviceName;

    @Column(name = "execution_time", nullable = false)
    private Long executionTime;
}
