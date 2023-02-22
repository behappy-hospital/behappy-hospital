package org.xiaowu.behappy.gateway.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.common.core.result.ResultCodeEnum;
import org.xiaowu.behappy.common.core.util.JwtHelper;
import reactor.core.publisher.Mono;

/**
 * @author xiaowu
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 请求路径
        String path = request.getURI().getPath();
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        // 内部服务接口,不允许外部访问
        if (antPathMatcher.match("/**/inner/**", path)) {
            return output(serverHttpResponse, ResultCodeEnum.PERMISSION);
        }
        // 放行接口
        if (antPathMatcher.match("/**/swagger-ui/**", path)
                || antPathMatcher.match("/swagger-ui.html", path)
                || antPathMatcher.match("/**/v3/api-docs", path)
                || antPathMatcher.match("/api/user/login", path)
                || antPathMatcher.match("/api/hosp/hospital/**", path)
                || antPathMatcher.match("/admin/cmn/dict/**", path)
                || antPathMatcher.match("/api/ucenter/**", path)
                || antPathMatcher.match("/api/msm/send/**", path)
                || antPathMatcher.match("/admin/**", path)) {
            return chain.filter(exchange);
        }
        String token = this.getToken(request);
        if (ObjectUtil.isNull(token)){
            return output(serverHttpResponse,ResultCodeEnum.LOGIN_AUTH);
        }
        // 如果请求头中有令牌则解析令牌
        try {
            JwtHelper.getUserName(token);
        } catch (Exception e) {
            // 解析jwt令牌出错, 说明令牌过期或者伪造等不合法情况出现
            log.error("解析令牌报错: {}",e.getMessage());
            return output(serverHttpResponse,ResultCodeEnum.PERMISSION);
        }
        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    @SneakyThrows
    private Mono<Void> output(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
        Result<Object> res = Result.restResult(null, resultCodeEnum.getCode(), resultCodeEnum.getMessage());
        byte[] bytes = objectMapper.writeValueAsBytes(res);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        // 指定编码, 否则浏览器中会出现乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 获取token
     * @apiNote
     * @author xiaowu
     * @return token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isEmpty(token)) {
            return null;
        }
        return token;
    }
}
