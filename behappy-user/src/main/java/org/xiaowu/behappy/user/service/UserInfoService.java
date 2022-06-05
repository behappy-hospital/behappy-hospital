package org.xiaowu.behappy.user.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.api.user.model.UserInfo;
import org.xiaowu.behappy.api.user.vo.LoginVo;
import org.xiaowu.behappy.common.core.exception.HospitalException;
import org.xiaowu.behappy.common.core.result.ResultCodeEnum;
import org.xiaowu.behappy.common.core.util.JwtHelper;
import org.xiaowu.behappy.user.mapper.UserInfoMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaowu
 */
@Service
@RequiredArgsConstructor
public class UserInfoService extends ServiceImpl<UserInfoMapper, UserInfo> implements IService<UserInfo> {

    private final RedisTemplate<String,String> redisTemplate;

    public Map<String, Object> login(LoginVo loginVo) {
        // 手机号
        String phone = loginVo.getPhone();
        // 验证码
        String code = loginVo.getCode();
        // 参数校验
        if (StrUtil.isEmpty(phone) || StrUtil.isEmpty(code)) {
            throw new HospitalException(ResultCodeEnum.PARAM_ERROR);
        }
        // 校验验证码
        String redisCode = redisTemplate.opsForValue().get(phone);
        if (!code.equals(redisCode)) {
            throw new HospitalException(ResultCodeEnum.CODE_ERROR);
        }
        // 手机号已被使用
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getPhone, phone);
        // 查询会员
        UserInfo userInfo = this.getOne(queryWrapper);
        // 如果为空则先保存
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setPhone(phone);
            userInfo.setStatus(1);
            this.save(userInfo);
        }
        // 校验是否被禁用
        if (userInfo.getStatus() == 0) {
            throw new HospitalException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        // todo 记录登录
        // 返回页面显示名称
        Map<String, Object> info = new HashMap<>();
        String name = userInfo.getName();
        if (StrUtil.isEmpty(name)){
            name = userInfo.getNickName();
        }
        if(StrUtil.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        info.put("name", name);
        String token = JwtHelper.createToken(userInfo.getId(), userInfo.getName());
        info.put("token", token);
        return info;
    }
}
