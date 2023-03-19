package com.nanal.backend.domain.auth.feign;

import com.nanal.backend.domain.auth.feign.resp.Keys;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "appleClient", url = "https://appleid.apple.com/auth")
public interface AppleFeignClient {

    // todo: 요청 캐싱
    @Cacheable(cacheNames = "appleOICD", cacheManager = "oidcCacheManager")
    @GetMapping(value = "/keys")
    Keys getKeys();

    @GetMapping(value = "/token")
    String getAccessToken();
}
