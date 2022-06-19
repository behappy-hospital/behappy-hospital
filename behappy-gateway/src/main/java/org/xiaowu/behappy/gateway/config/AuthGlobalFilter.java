package org.xiaowu.behappy.gateway.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.common.core.result.ResultCodeEnum;
import org.xiaowu.behappy.common.core.util.JwtHelper;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author xiaowu
 */
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        // 内部服务接口,不允许外部访问
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        if (antPathMatcher.match("/**/inner/**", path)) {
            return output(serverHttpResponse, ResultCodeEnum.PERMISSION);
        }
        //Long userId = this.getUserId(request);
        // api接口, 用户必须登录, 除开登录接口
        // todo 这里需要对不需要验证的接口做下统计
        //if (antPathMatcher.match("/api/api/**", path)) {
        //    // 剔除登录接口和发送短信接口
        //    if (antPathMatcher.match("**/login/**", path)
        //            || antPathMatcher.match("**/send/**", path)
        //            || antPathMatcher.match("/api/api/**", path)) {
        //        return chain.filter(exchange);
        //    }
        //    if (ObjectUtil.isNull(userId)){
        //        return output(serverHttpResponse,ResultCodeEnum.LOGIN_AUTH);
        //    }
        //}
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
     * 获取当前登录用户id
     * @apiNote
     * @author xiaowu
     * @param request
     * @return long
     */
    private Long getUserId(ServerHttpRequest request) {
        List<String> tokenList = request.getHeaders().get("token");
        String token = null;
        if (CollUtil.isNotEmpty(tokenList)) {
            token = tokenList.get(0);
        }
        if (StrUtil.isEmpty(token)) {
            return null;
        }
        return JwtHelper.getUserId(token);
    }
}
