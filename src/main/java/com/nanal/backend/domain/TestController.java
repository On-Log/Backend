package com.nanal.backend.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TestController {
    private final RedisTemplate<String, String> redisTemplate;

    @GetMapping("/test/log")
    public void logTest() {
        log.info("info");
        log.warn("warn");
        log.error("error");
    }

    @GetMapping("/test/gc")
    public void gcTest(@PathParam("loop") Integer loop) {

        long initHeapSize = Long.parseLong(System.getProperty("sun.arch.data.model")) == 32 ?
                Integer.getInteger("sun.arch.data.model") : Long.getLong("sun.arch.data.model");
        System.out.println("Initial Heap Size: " + initHeapSize / 1024 / 1024 + "MB");


        // 최대 힙 사이즈 (Xmx 옵션으로 설정한 값)
        long maxHeapSize = Runtime.getRuntime().maxMemory();
        System.out.println("Max Heap Size: " + maxHeapSize / 1024 / 1024 + "MB");

        String tmp = null;
        for (int i = 0; i < loop; i++) {
            tmp = new String("gc");
        }

        System.out.println(tmp);
    }
}