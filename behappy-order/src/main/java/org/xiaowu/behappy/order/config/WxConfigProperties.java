package org.xiaowu.behappy.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author xiaowu
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wx.pay")
public class WxConfigProperties {

    private String appId;

    private String partner;

    private String partnerKey;

    private String cert;
}