package org.xiaowu.behappy.common.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 在工具化的应用场景下，想要动态获取bean,使用SpringUtil
 * @author xiaowu
 */
@Configuration
@ComponentScan(basePackages = {"org.xiaowu.behappy"})
@Import(cn.hutool.extra.spring.SpringUtil.class)
public class GlobalSpringConfigure {

}
