package org.xiaowu.behappy.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云sms配置模板
 *
 * @author xiaowu
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.sms")
public class AliSmsProperties {

    private String templateCode;

}
