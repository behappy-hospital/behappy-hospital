package org.xiaowu.behappy.common.sentinel.parser;

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
	private static final String ALLOW = "Origin";

	/**
	 * Parse the origin from given HTTP request.
	 * @param request HTTP request
	 * @return parsed origin
	 */
	@Override
	public String parseOrigin(HttpServletRequest request) {
		return request.getHeader(ALLOW);
	}

}
