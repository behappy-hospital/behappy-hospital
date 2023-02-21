package org.xiaowu.behappy.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaowu.behappy.api.user.vo.UserInfoQueryVo;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.user.entity.UserInfo;
import org.xiaowu.behappy.user.service.UserInfoService;

import java.util.Map;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserController {

    private final UserInfoService userInfoService;

    //用户列表（条件查询带分页）
    @GetMapping("/{page}/{limit}")
    public Result<IPage<UserInfo>> list(@PathVariable Long page,
                                        @PathVariable Long limit,
                                        UserInfoQueryVo userInfoQueryVo) {
        Page<UserInfo> pageParam = new Page<>(page, limit);
        IPage<UserInfo> pageModel =
                userInfoService.selectPage(pageParam, userInfoQueryVo);
        return Result.ok(pageModel);
    }

    @Operation(summary = "锁定")
    @GetMapping("/lock/{userId}/{status}")
    public Result<Boolean> lock(
            @PathVariable("userId") Long userId,
            @PathVariable("status") Integer status) {
        userInfoService.lock(userId, status);
        return Result.ok();
    }

    /**
     * 用户详情
     * @apiNote
     * @author xiaowu
     * @param userId
     * @return org.xiaowu.behappy.common.core.result.Response<java.util.Map < java.lang.String, java.lang.Object>>
     */
    @GetMapping("/show/{userId}")
    public Result<Map<String, Object>> show(@PathVariable Long userId) {
        Map<String, Object> map = userInfoService.show(userId);
        return Result.ok(map);
    }

    /**
     * 认证审批
     * @apiNote
     * @author xiaowu
     * @param userId
     * @param authStatus
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @GetMapping("/approval/{userId}/{authStatus}")
    public Result<Boolean> approval(@PathVariable Long userId, @PathVariable Integer authStatus) {
        userInfoService.approval(userId, authStatus);
        return Result.ok(true);
    }


}
