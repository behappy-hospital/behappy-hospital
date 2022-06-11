package org.xiaowu.behappy.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author xiaowu
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wx.open")
public class WxConfigProperties {

    private String appId;

    private String appSecret;

    private String redirectUrl;

    private String yyghBaseUrl;
}
