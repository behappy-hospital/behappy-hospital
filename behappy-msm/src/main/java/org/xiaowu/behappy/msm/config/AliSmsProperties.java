package org.xiaowu.behappy.msm.config;

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

    private String accessKeyId = "LTAI4G2h6DW4DCwJN4TWJNbH";

    private String accessKeySecret = "OJD6uoCm5Rfn0ZLw9zcBbhzSi6e7jF";

    private String signName = "ABC商城";

    private String templateCode = "SMS_206546316";

}
