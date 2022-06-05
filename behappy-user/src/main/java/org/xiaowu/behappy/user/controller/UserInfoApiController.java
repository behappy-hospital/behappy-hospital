package org.xiaowu.behappy.user.controller;

import cn.hutool.core.net.Ipv4Util;
import com.alibaba.nacos.common.utils.IPUtil;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaowu.behappy.api.user.vo.LoginVo;
import org.xiaowu.behappy.common.core.result.Response;
import org.xiaowu.behappy.common.core.util.IpUtil;
import org.xiaowu.behappy.user.service.UserInfoService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户
 * @author xiaowu
 */
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserInfoApiController {

    private final UserInfoService userInfoService;

    @ApiOperation(value = "会员登录")
    @PostMapping("/login")
    public Response<Map<String, Object>> login(@RequestBody LoginVo loginVo,
                                               HttpServletRequest request) {
        loginVo.setIp(IpUtil.getIpAddr(request));
        Map<String, Object> info = userInfoService.login(loginVo);
        return Response.ok(info);
    }
}
