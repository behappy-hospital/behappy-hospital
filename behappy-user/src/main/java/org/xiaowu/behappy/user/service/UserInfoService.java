package org.xiaowu.behappy.user.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.api.user.enums.AuthStatusEnum;
import org.xiaowu.behappy.api.user.vo.LoginVo;
import org.xiaowu.behappy.api.user.vo.UserAuthVo;
import org.xiaowu.behappy.api.user.vo.UserInfoQueryVo;
import org.xiaowu.behappy.common.core.exception.HospitalException;
import org.xiaowu.behappy.common.core.result.ResultCodeEnum;
import org.xiaowu.behappy.common.core.util.IpUtil;
import org.xiaowu.behappy.common.core.util.JwtHelper;
import org.xiaowu.behappy.user.entity.Patient;
import org.xiaowu.behappy.user.entity.UserInfo;
import org.xiaowu.behappy.user.entity.UserLoginRecord;
import org.xiaowu.behappy.user.mapper.UserInfoMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaowu
 */
@Service
@AllArgsConstructor
public class UserInfoService extends ServiceImpl<UserInfoMapper, UserInfo> implements IService<UserInfo> {

    private final RedisTemplate<String, String> redisTemplate;

    private final PatientService patientService;

    private final UserLoginRecordService userLoginRecordService;

    public UserInfo getByOpenid(String openid) {
        return baseMapper.selectOne(new QueryWrapper<UserInfo>().eq("openid", openid));
    }

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
        //绑定手机号码
        UserInfo userInfo = null;
        if (StrUtil.isNotEmpty(loginVo.getOpenid())) {
            userInfo = this.getByOpenid(loginVo.getOpenid());
            if (null != userInfo) {
                userInfo.setPhone(loginVo.getPhone());
                this.updateById(userInfo);
            } else {
                throw new HospitalException(ResultCodeEnum.DATA_ERROR);
            }
        }
        if (userInfo == null) {
            // 手机号已被使用
            LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserInfo::getPhone, phone);
            // 查询会员
            userInfo = this.getOne(queryWrapper);
            // 如果为空则先保存
            if (userInfo == null) {
                userInfo = new UserInfo();
                userInfo.setName("");
                userInfo.setPhone(phone);
                userInfo.setStatus(1);
                this.save(userInfo);
            }
        }
        // 登录记录
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(userInfo.getId());
        userLoginRecord.setIp(loginVo.getIp());
        userLoginRecord.setIsDeleted(userInfo.getIsDeleted());
        userLoginRecordService.save(userLoginRecord);

        // 校验是否被禁用
        if (userInfo.getStatus() == 0) {
            throw new HospitalException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        // 返回页面显示名称
        Map<String, Object> info = new HashMap<>();
        String name = userInfo.getName();
        if (StrUtil.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StrUtil.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        info.put("name", name);
        String token = JwtHelper.createToken(String.valueOf(userInfo.getId()), userInfo.getName());
        info.put("token", token);
        return info;
    }

    public void userAuth(Long userId, UserAuthVo userAuthVo) {
        //根据用户id查询用户信息
        UserInfo userInfo = baseMapper.selectById(userId);
        //设置认证信息
        //认证人姓名
        userInfo.setName(userAuthVo.getName());
        //其他认证信息
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        //进行信息更新
        baseMapper.updateById(userInfo);
    }

    public IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo) {
        //UserInfoQueryVo获取条件值
        String name = userInfoQueryVo.getKeyword(); //用户名称
        Integer status = userInfoQueryVo.getStatus();//用户状态
        Integer authStatus = userInfoQueryVo.getAuthStatus(); //认证状态
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin(); //开始时间
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd(); //结束时间
        //对条件值进行非空判断
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        if(!StrUtil.isEmpty(name)) {
            wrapper.like("name",name);
        }
        if(!ObjectUtil.isEmpty(status)) {
            wrapper.eq("status",status);
        }
        if(!ObjectUtil.isEmpty(authStatus)) {
            wrapper.eq("auth_status",authStatus);
        }
        if(!StrUtil.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time",createTimeBegin);
        }
        if(!StrUtil.isEmpty(createTimeEnd)) {
            wrapper.le("create_time",createTimeEnd);
        }
        //调用mapper的方法
        IPage<UserInfo> pages = baseMapper.selectPage(pageParam, wrapper);
        //编号变成对应值封装
        pages.getRecords().stream().forEach(item -> {
            this.packageUserInfo(item);
        });
        return pages;
    }

    //编号变成对应值封装
    private UserInfo packageUserInfo(UserInfo userInfo) {
        //处理认证状态编码
        userInfo.getParam().put("authStatusString",AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        //处理用户状态 0  1
        String statusString = userInfo.getStatus() ==0 ?"锁定" : "正常";
        userInfo.getParam().put("statusString",statusString);
        return userInfo;
    }

    public void lock(Long userId, Integer status) {
        if(status == 0 || status == 1) {
            UserInfo userInfo = this.getById(userId);
            userInfo.setStatus(status);
            this.updateById(userInfo);
        }
    }

    public Map<String, Object> show(Long userId) {
        Map<String,Object> map = new HashMap<>();
        //根据userid查询用户信息
        UserInfo userInfo = this.packageUserInfo(baseMapper.selectById(userId));
        map.put("userInfo",userInfo);
        //根据userid查询就诊人信息
        List<Patient> patientList = patientService.findAllUserId(userId);
        map.put("patientList",patientList);
        return map;
    }

    public void approval(Long userId, Integer authStatus) {
        if(authStatus==2 || authStatus==-1) {
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setAuthStatus(authStatus);
            baseMapper.updateById(userInfo);
        }
    }

}
