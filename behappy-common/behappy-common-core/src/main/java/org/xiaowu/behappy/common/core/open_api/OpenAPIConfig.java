package org.xiaowu.behappy.common.core.open_api;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.xiaowu.behappy.common.core.constants.CommonConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http://localhost:8088/swagger-ui.html
 * @author 94391
 */
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Configuration
@RequiredArgsConstructor
public class OpenAPIConfig {

    public static final String API_URI = "/v3/api-docs";

    /**
     * host需要配置成网关的地址
     */
    @Value("${swagger.host:http://localhost:8088}")
    private String host;

    /**
     * 项目应用名
     */
    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 项目版本信息
     */
    @Value("${spring.application.version:1.0.0}")
    private String applicationVersion;

    /**
     * 项目描述信息
     */
    @Value("${spring.application.description:尚医通(改)}")
    private String applicationDescription;


    /**
     * 一定要用""把map所对应的value包起来
     */
    //@Value("#{${swagger.services}}")
    //private Map<String, String> services;

    @Bean
    public OpenAPI springOpenAPI() {
        OpenAPI openAPI = new OpenAPI().info(new Info()
                .title(applicationName)
                .version(applicationVersion)
                .description(applicationDescription)
                .contact(new Contact()
                        .name("小五")
                        .url("https://wang-xiaowu.github.io/")
                        .email("a943915349@gmail.com")));
        openAPI.schemaRequirement(HttpHeaders.AUTHORIZATION, this.securityScheme());
        openAPI.addServersItem(new Server()
                .url(host + String.format("/%s", applicationName))
                .description(applicationDescription));
        return openAPI;
    }

    private SecurityScheme securityScheme() {
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setType(SecurityScheme.Type.APIKEY);
        securityScheme.setName(HttpHeaders.AUTHORIZATION);
        securityScheme.in(SecurityScheme.In.HEADER);
        return securityScheme;
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group(applicationName)
                .packagesToScan("org.xiaowu.behappy")
                .build();
    }
}
