package org.xiaowu.behappy.msm.controller.api;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.msm.service.MsmService;

import java.util.concurrent.TimeUnit;

/**
 * 短信
 * @author xiaowu
 */
@RestController
@RequestMapping("/api/msm")
@AllArgsConstructor
public class MsmApiController {

    private final MsmService msmService;

    private final RedisTemplate<String, String> redisTemplate;

    @GetMapping("/send/{phone}")
    public Result<Boolean> sendMsm(@PathVariable String phone) {
        // 从redis获取验证码, 如果有, 则ok(说明两分钟内有获取到验证码, 前端定时器)
        // 验证码code
        String code = redisTemplate.opsForValue().get(phone);
        if (StrUtil.isNotEmpty(code)) {
            return Result.failed("发生验证码过于频繁");
        }
        // 如果从redis获取不到, 则生成验证码
        code = RandomUtil.randomNumbers(6);
        // 通过阿里云sms发送短信
        boolean successSend = msmService.sendSms(phone,code);
        if (!successSend) {
            return Result.failed("发送短信失败");
        }
        // 将验证码设置2分钟的有效期
        redisTemplate.opsForValue().set(phone,code,2, TimeUnit.MINUTES);
        return Result.ok();
    }
}
