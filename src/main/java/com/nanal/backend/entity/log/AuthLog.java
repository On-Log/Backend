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
public class AuthLog extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_log_id")
    private Long AuthLogId;

    @Column(name = "user_email", nullable = false, length = 50)
    private String userEmail;

    @Column(name = "service_name", nullable = false, length = 30)
    private String serviceName;

}
