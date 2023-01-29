package org.xiaowu.behappy.gateway.open_api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.xiaowu.behappy.common.core.open_api.OpenAPIConfig.API_URI;

/**
 * http://localhost:8088/swagger-ui.html
 * @author 94391
 */
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
@Configuration
@RequiredArgsConstructor
public class OpenAPIConfig  implements InitializingBean {

    /**
     * RouteLocator，GatewayProperties这两个类都是springcloud提供的springbean对象直接注入即可
     */
    private final RouteLocator routeLocator;

    /**
     * gateway配置文件
     */
    private final GatewayProperties gatewayProperties;

    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> swaggerUrlSet = new HashSet<>();
        // 非阻塞式
        routeLocator.getRoutes().subscribe(route -> {
            //从配置文件中获取并配置SwaggerResource
            gatewayProperties.getRoutes().stream()
                    //过滤路由
                    .filter(routeDefinition -> route.getId().equals(routeDefinition.getId()))
                    //循环添加，从路由的断言中获取，一般来说路由都会配置断言Path信息，这就不多说了
                    .forEach(gatewayRoute -> {
                        gatewayRoute.getPredicates().stream()
                                //这里location必须配置为${routeId}
                                .filter(predicateDefinition -> "Path".equalsIgnoreCase(predicateDefinition.getName()))
                                //开始添加SwaggerResource
                                .forEach(predicateDefinition -> {
                                    String routeId = gatewayRoute.getId();
                                    if (StrUtil.isNotEmpty(routeId)) {
                                        AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = assembleResource(routeId, String.format("/%s", routeId + API_URI));
                                        swaggerUrlSet.add(swaggerUrl);
                                    }
                                });
                    });
            swaggerUiConfigProperties.setUrls(swaggerUrlSet);
        });

    }

    private AbstractSwaggerUiConfigProperties.SwaggerUrl assembleResource(String name, String location) {
        AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl();
        swaggerUrl.setName(name);
        swaggerUrl.setUrl(location);
        swaggerUrl.setDisplayName(name);
        return swaggerUrl;
    }
}
