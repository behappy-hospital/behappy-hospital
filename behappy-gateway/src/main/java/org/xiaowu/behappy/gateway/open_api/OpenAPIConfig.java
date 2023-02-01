package org.xiaowu.behappy.gateway.open_api;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

import static org.xiaowu.behappy.common.core.open_api.OpenAPIConfig.API_URI;

/**
 * http://localhost:8088/swagger-ui.html
 * @author 94391
 */
@ConditionalOnExpression("#{'dev'.equals('${spring.profiles.active:}')}")
@Configuration
@RequiredArgsConstructor
public class OpenAPIConfig implements InitializingBean {

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
        gatewayProperties.getRoutes().forEach(route -> {
            String routeId = route.getId();
            if (StrUtil.isNotEmpty(routeId) && StrUtil.startWith(routeId, "behappy-")) {
                AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = assembleResource(routeId, String.format("/%s", routeId + API_URI));
                swaggerUrlSet.add(swaggerUrl);
            }
        });
        swaggerUiConfigProperties.setUrls(swaggerUrlSet);
    }

    private AbstractSwaggerUiConfigProperties.SwaggerUrl assembleResource(String name, String location) {
        AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl();
        swaggerUrl.setName(name);
        swaggerUrl.setUrl(location);
        swaggerUrl.setDisplayName(name);
        return swaggerUrl;
    }
}
