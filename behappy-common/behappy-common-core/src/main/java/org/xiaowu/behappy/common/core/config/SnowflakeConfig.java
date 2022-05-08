package org.xiaowu.behappy.common.core.config;

import cn.hutool.core.lang.Snowflake;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/**
 * 雪花算法配置数据中心和机器编号，不同机器组合不能重复
 * @author 小五
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "snowflake")
public class SnowflakeConfig {

    /**
     * 机器标识[0,31] 配置文件中不配置就是0
     */
    private long machineId = 0L;

    /**
     * 数据中心[0,31] 配置文件中不配置就是0
     */
    private long dataCenterId = 0L;


    @Bean
    @Primary
    @Scope("singleton")
    public Snowflake snowflake() {
        Snowflake snowflake = new Snowflake(machineId, dataCenterId, true);
        return snowflake;
    }
}