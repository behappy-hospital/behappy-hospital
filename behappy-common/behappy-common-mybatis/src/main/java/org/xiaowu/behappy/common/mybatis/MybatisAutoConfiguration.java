/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.xiaowu.behappy.common.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.MySqlDialect;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.xiaowu.behappy.common.mybatis.config.BeHappyMetaObjectHandler;
import org.xiaowu.behappy.common.mybatis.resolver.SqlFilterArgumentResolver;

import java.util.List;

/**
 * mybatis plus 统一配置
 * @author xiaowu
 */
@MapperScan({"org.xiaowu.behappy.**.mapper"})
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class MybatisAutoConfiguration implements WebMvcConfigurer {

    private final Environment environment;

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new BeHappyMetaObjectHandler();
    }

    /**
     * 重写mp的雪花id的dataCenterId和workerId
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public IdentifierGenerator identifierGenerator() {
        long dataCenterId = Long.parseLong(environment.getProperty("snowflake.data-center-id", "0"));
        long workerId = Long.parseLong(environment.getProperty("snowflake.machine-id", "0"));
        return new DefaultIdentifierGenerator(workerId, dataCenterId);
    }

    /**
     * SQL 过滤器避免SQL 注入
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SqlFilterArgumentResolver());
    }

    /**
     * 插件配置,
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 向Mybatis过滤器链中添加分页拦截器
        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor();
        // 对于单一数据库类型来说,都建议配置该值,避免每次分页都去抓取数据库类型
        innerInterceptor.setDbType(DbType.MYSQL);
        innerInterceptor.setDialect(new MySqlDialect());
        // 如果查询当前页大于总页数，则置为第一页
        innerInterceptor.setOverflow(true);
        interceptor.addInnerInterceptor(innerInterceptor);
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }


}
