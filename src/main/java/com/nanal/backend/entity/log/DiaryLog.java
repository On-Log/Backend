package com.nanal.backend.entity.log;

import com.nanal.backend.entity.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DiaryLog extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_log_id")
    private Long DiaryLogId;

    @Column(name = "user_email", nullable = false, length = 50)
    private String userEmail;

    @Column(name = "service_name", nullable = false, length = 30)
    private String serviceName;

    @Column(name = "execution_time", nullable = false)
    private Long executionTime;
}
