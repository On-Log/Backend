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
public class RetrospectLog extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retrospect_log_id")
    private Long retrospectLogId;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "execution_time")
    private Long executionTime;
}
