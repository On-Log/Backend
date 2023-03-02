package com.nanal.backend.global.throttling;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
@Component
public class UserRateLimiter {

    private final ProxyManager<String> buckets;

    @Value("${throttle.capacity}")
    private long capacity;

    @Value("${throttle.greedyRefill}")
    private long greedyRefill;

    public Bucket resolveBucket(String key) {
        Supplier<BucketConfiguration> configSupplier = getConfigSupplierForUser();
        return buckets.builder().build(key, configSupplier);
    }

    private Supplier<BucketConfiguration> getConfigSupplierForUser() {
        Refill refill = Refill.greedy(greedyRefill, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        return () -> (BucketConfiguration.builder().addLimit(limit).build());
    }
}