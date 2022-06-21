package org.xiaowu.behappy.order;

import cn.hutool.core.util.NumberUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

//@SpringBootTest
class BehappyOrderApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(NumberUtil.round(new BigDecimal(1),2).toString());;
    }

}
