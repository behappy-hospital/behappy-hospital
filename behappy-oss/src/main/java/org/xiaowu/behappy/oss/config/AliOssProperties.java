package org.xiaowu.behappy.oss.config;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaowu
 * oss配置模板
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliOssProperties {

    private String endpoint;

    private String bucket;

    private String accessKeyId;

    private String accessKeySecret;

    @Bean(name = "ossClient",destroyMethod = "shutdown")
    public OSSClient ossClient() {
        OSSClient ossClient = (OSSClient) new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        return ossClient;
    }
}