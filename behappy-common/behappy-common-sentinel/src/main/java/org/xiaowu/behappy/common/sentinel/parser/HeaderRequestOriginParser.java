package org.xiaowu.behappy.common.sentinel.parser;

import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;

/**
 * sentinel 请求头解析判断
 * @author 94391
 */
public class HeaderRequestOriginParser implements RequestOriginParser {

    /**
     * 请求头获取allow
     */
    private static final String ALLOW = "origin";

    /**
     * 默认情况下，sentinel不管请求者从哪里来，返回值永远是default，也就是说一切请求的来源都被认为是一样的值default。
     * 因此，我们需要自定义这个接口的实现，让不同的请求，返回不同的origin。
     * Parse the origin from given HTTP request.
     * @param request HTTP request
     * @return parsed origin
     */
    @Override
    public String parseOrigin(HttpServletRequest request) {
        String header = request.getHeader(ALLOW);
        return StrUtil.isEmpty(header) ? "blank" : header;
    }

}
