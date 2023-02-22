package org.xiaowu.behappy.common.core.open_api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * http://localhost:8088/swagger-ui.html
 * @author 94391
 */
@Data
@ConditionalOnExpression("#{'dev'.equals('${spring.profiles.active:}')}")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.application")
public class OpenAPIConfig {

    public static final String API_URI = "/v3/api-docs";

    /**
     * 项目应用名
     */
    private String name;

    /**
     * host需要配置成网关的地址
     */
    private String gatewayHost;

    /**
     * 项目版本信息
     */
    private String version;

    /**
     * 项目描述信息
     */
    private String description;

    @Bean
    public OpenAPI springOpenAPI() {
        OpenAPI openAPI = new OpenAPI().info(new Info()
                .title(name)
                .version(version)
                .description(description)
                .contact(new Contact()
                        .name("小五")
                        .url("https://wang-xiaowu.github.io/")
                        .email("a943915349@gmail.com")));
        openAPI.schemaRequirement(HttpHeaders.AUTHORIZATION, this.securityScheme());
        // 全局安全校验项，也可以在对应的controller上加注解@SecurityRequirement
        openAPI.addSecurityItem(new SecurityRequirement()
                .addList(HttpHeaders.AUTHORIZATION));
        // 添加对应server
        openAPI.addServersItem(new Server()
                .url(gatewayHost + String.format("/%s", name))
                .description(description));
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
                .group(name)
                .packagesToScan("org.xiaowu.behappy")
                .build();
    }
}
