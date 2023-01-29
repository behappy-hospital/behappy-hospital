package org.xiaowu.behappy.user.controller.api;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.xiaowu.behappy.api.user.vo.LoginVo;
import org.xiaowu.behappy.api.user.vo.UserAuthVo;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.common.core.util.AuthContextHolder;
import org.xiaowu.behappy.common.core.util.IpUtil;
import org.xiaowu.behappy.user.entity.UserInfo;
import org.xiaowu.behappy.user.service.UserInfoService;

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

    @Operation(summary = "会员登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginVo loginVo,
                                             HttpServletRequest request) {
        loginVo.setIp(IpUtil.getIpAddr(request));
        Map<String, Object> info = userInfoService.login(loginVo);
        return Result.ok(info);
    }

    /**
     * 用户认证接口
     * @apiNote
     * @author xiaowu
     * @param userAuthVo
     * @param request
     * @return org.xiaowu.behappy.common.core.result.Response
     */
    @PostMapping("/auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request) {
        //传递两个参数，第一个参数用户id，第二个参数认证数据vo对象
        userInfoService.userAuth(AuthContextHolder.getUserId(request), userAuthVo);
        return Result.ok();
    }

    /**
     * 获取用户id信息接口
     * @apiNote
     * @author xiaowu
     * @param request
     * @return org.xiaowu.behappy.common.core.result.Response
     */
    @GetMapping("/auth/getUserInfo")
    public Result<UserInfo> getUserInfo(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }

}
