package org.xiaowu.behappy.common.core.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * DocumentationType，有三个版本，swagger3会丢失网关的访问的前缀。比如我们通过网关访问/fvsSysUser/list，
 * 会自动添加服务名为前缀，请求应该是这样的http://localhost:2626/fvs-admin/fvsSysUser/list,
 * 但是，swagger3会丢失服务名请求就成了http://localhost:2626/fvsSysUser/list
 * 而swagger2会保留前缀
 * 这两个版本的ui差距不大,依据实际需求使用
 *
 * UI：http://localhost:8088/swagger-ui/index.html
 * @author xiaowu
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Configuration
public class Swagger3Config {

    /**
     * host需要配置成网关的地址
     */
    @Value("${swagger.host:localhost:8088}")
    private String host;

    /**
     * 项目应用名
     */
    @Value("${spring.application.name}")
    private String applicationName;


    /**
     * 是否开启swagger，生产环境一般关闭，所以这里定义一个变量
     */
    @Value("${swagger.enable:true}")
    private Boolean enable;

    /**
     * 项目版本信息
     */
    @Value("${swagger.application.version:1.0.0}")
    private String applicationVersion;

    /**
     * 项目描述信息
     */
    @Value("${swagger.application.description:医院模块}")
    private String applicationDescription;

    /**
     * 解决swagger在springboot2.6.x下不可用的问题
     * https://stackoverflow.com/questions/40241843/failed-to-start-bean-documentationpluginsbootstrapper-in-spring-data-rest
     * @param webEndpointsSupplier
     * @param servletEndpointsSupplier
     * @param controllerEndpointsSupplier
     * @param endpointMediaTypes
     * @param corsProperties
     * @param webEndpointProperties
     * @param environment
     * @return
     */
    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping =
                webEndpointProperties.getDiscovery().isEnabled() &&
                        (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
    }

    @Bean
    public Docket apiConfig() {
        return new Docket(DocumentationType.OAS_30)
                .groupName(applicationName)
                .apiInfo(apiInfo())
                .host(host)
                .enable(enable)
                .globalRequestParameters(globalRequestParameters())
                .select()
                //.apis() 控制哪些接口暴露给swagger，
                // RequestHandlerSelectors.any() 所有都暴露
                // RequestHandlerSelectors.basePackage("org.xiaowu.behappy")  指定包位置
                // withMethodAnnotation(ApiOperation.class)标记有这个注解 ApiOperation
                .apis(RequestHandlerSelectors.basePackage("org.xiaowu.behappy"))
                // 控制显式那些页面
                //.paths()
                // PathSelectors.regex("/api/.*") 只显示api路径下的页面
                // PathSelectors.any() 显式所有
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(applicationName)
                .description(applicationDescription)
                .version(applicationVersion)
                .contact(new Contact("小五", "https://wang-xiaowu.github.io/", "a943915349@gmail.com"))
                .build();
    }


    /**
     * 为每个请求添加Authorization请求头
     * @return
     */
    private List<RequestParameter> globalRequestParameters() {
        List<RequestParameter> requestParameters = new ArrayList<>();
        RequestParameterBuilder builder = new RequestParameterBuilder();
        requestParameters.add(
                builder.name("Authorization")
                        .required(false)
                        .in(ParameterType.HEADER)
                        .build()
        );
        return requestParameters;
    }
}
