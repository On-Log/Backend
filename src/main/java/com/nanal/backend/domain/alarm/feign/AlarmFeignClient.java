package com.nanal.backend.domain.alarm.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "alarm", url = "${firebase.push.url}")
public interface AlarmFeignClient {
}
