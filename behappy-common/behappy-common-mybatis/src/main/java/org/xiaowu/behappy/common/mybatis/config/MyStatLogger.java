package org.xiaowu.behappy.common.mybatis.config;

import com.alibaba.druid.pool.DruidDataSourceStatLogger;
import com.alibaba.druid.pool.DruidDataSourceStatLoggerAdapter;
import com.alibaba.druid.pool.DruidDataSourceStatValue;
import com.alibaba.druid.support.logging.Log;

import java.util.Properties;

/**
 * todo druid 日志记录
 * @author 94391
 */
public class MyStatLogger extends DruidDataSourceStatLoggerAdapter implements DruidDataSourceStatLogger {
    @Override
    public void log(DruidDataSourceStatValue statValue) {
        super.log(statValue);
    }

    @Override
    public void configFromProperties(Properties properties) {
        super.configFromProperties(properties);
    }

    @Override
    public void setLogger(Log logger) {
        super.setLogger(logger);
    }

    @Override
    public void setLoggerName(String loggerName) {
        super.setLoggerName(loggerName);
    }
}
