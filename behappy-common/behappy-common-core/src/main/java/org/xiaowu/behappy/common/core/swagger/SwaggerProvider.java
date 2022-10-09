package org.xiaowu.behappy.common.core.swagger;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 94391
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Component
@Primary
public class SwaggerProvider implements SwaggerResourcesProvider {

    public static final String API_URI = "/v3/api-docs";

    /**
     * RouteLocator，GatewayProperties这两个类都是springcloud提供的springbean对象直接注入即可
     */
    private final RouteLocator routeLocator;

    /**
     * gateway配置文件
     */
    private final GatewayProperties gatewayProperties;


    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        //从配置文件中获取并配置SwaggerResource
        gatewayProperties.getRoutes().stream()
                //过滤路由
                .filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                //循环添加，从路由的断言中获取，一般来说路由都会配置断言Path信息，这就不多说了
                .forEach(route -> {
                    route.getPredicates().stream()
                            //这里location必须配置为${routeId}
                            .filter(predicateDefinition -> "Path".equalsIgnoreCase(predicateDefinition.getName()))
                            //开始添加SwaggerResource
                            .forEach(predicateDefinition -> {
                                String routeId = route.getId();
                                if (StringUtils.isNotEmpty(routeId)) {
                                    //resources.add(swaggerResource(routeId,
                                    //        predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                    //                .replace("/**", API_URI)));
                                    // 不拼接group参数，这种情况只在模块儿中配置的swagger没有指定groupName情况下可以，
                                    // 如果Docket配置了groupName，那这里就需要拼接group了
                                    String location = String.format("/%s?group=%s", routeId + API_URI, routeId);
                                    resources.add(swaggerResource(routeId, location));
                                }
                            });
                });

        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(DocumentationType.OAS_30.getVersion());
        return swaggerResource;
    }

    @Autowired
    public SwaggerProvider(RouteLocator routeLocator, GatewayProperties gatewayProperties) {
        this.routeLocator = routeLocator;
        this.gatewayProperties = gatewayProperties;
    }

}
 
