package com.nanal.backend.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class TestController {
    private final RedisTemplate<String, String> redisTemplate;

    @GetMapping("/main")
    public String main() {
        log.info("hi");
        return "index.html";
    }

    @ResponseBody
    @GetMapping("/auth")
    public String auth() {
        log.info("hi auth user");
        return "auth success";
    }

    @ResponseBody
    @GetMapping("/deploy")
    public String deploy() {
        log.info("hi deploy");
        return "deploy success";
    }

    @ResponseBody
    @GetMapping("/redis/test")
    public String store() {

        // given
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = "why";

        valueOperations.set(key, "hello");

        return "ok";
    }
}