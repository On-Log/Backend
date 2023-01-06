package com.nanal.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @SpringBootTest 활성화하면 contextLoads FAILED 에러 발생.
 *  test 에도 database 접근 정보 설정해야함.
 */
//@SpringBootTest
class NanalApplicationTests {

    @Test
    void contextLoads() {
    }

}
