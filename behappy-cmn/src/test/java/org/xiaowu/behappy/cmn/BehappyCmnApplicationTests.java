package org.xiaowu.behappy.cmn;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class BehappyCmnApplicationTests {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
        stringRedisTemplate.opsForValue().set("a","测试");
        System.out.println(stringRedisTemplate.opsForValue().get("a"));
    }

}
