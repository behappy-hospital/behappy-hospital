package org.xiaowu.behappy.user.controller.api;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xiaowu.behappy.common.core.exception.HospitalException;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.common.core.result.ResultCodeEnum;
import org.xiaowu.behappy.common.core.util.HttpClientUtil;
import org.xiaowu.behappy.common.core.util.HttpUtil;
import org.xiaowu.behappy.common.core.util.JwtHelper;
import org.xiaowu.behappy.user.config.WxConfigProperties;
import org.xiaowu.behappy.user.entity.UserInfo;
import org.xiaowu.behappy.user.service.UserInfoService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaowu
 */
@Controller
@RequestMapping("/api/ucenter/wx")
@AllArgsConstructor
@Slf4j
public class WeixinApiController {

    private final UserInfoService userInfoService;

    private final WxConfigProperties wxConfigProperties;

    /**
     * 获取微信登录参数
     * @apiNote
     * @author xiaowu
     * @return org.xiaowu.behappy.common.core.result.Response<java.util.Map < java.lang.String, java.lang.Object>>
     */
    @SneakyThrows
    @GetMapping("/getLoginParam")
    @ResponseBody
    public Result<Map<String, Object>> getQrConnect() {
        Map<String, Object> map = new HashMap<>();
        map.put("appid", wxConfigProperties.getAppId());
        map.put("redirectUri", URLEncoder.encode(wxConfigProperties.getRedirectUrl(), StandardCharsets.UTF_8));
        map.put("scope", "snsapi_login");
        map.put("state", System.currentTimeMillis());
        return Result.ok(map);
    }

    @SneakyThrows
    @RequestMapping("/callback")
    public String callback(String code, String state) {
        // 获取授权票据
        log.info("state: {}", state);
        log.info("code: {}", code);
        if (StrUtil.isEmpty(state) || StrUtil.isEmpty(code)) {
            log.error("非法回调请求");
            throw new HospitalException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }
        // 使用code和appid以及appsecret换取access_token
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");

        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                wxConfigProperties.getAppId(),
                wxConfigProperties.getAppSecret(),
                code);

        JSONObject jsonObject = HttpClientUtil.doHttpGet(accessTokenUrl, null);
        log.info("使用token换取的结果: {}", jsonObject.toString());
        if (jsonObject.getString("errcode") != null) {
            log.error("获取token失败: {}", jsonObject.getString("errmsg"));
            throw new HospitalException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        //根据access_token获取微信用户的基本信息
        String access_token = jsonObject.getString("access_token");
        String openid = jsonObject.getString("openid");
        //先根据openid进行数据库查询
        UserInfo userInfo = userInfoService.getByOpenid(openid);
        // 如果没有查到用户信息,那么调用微信个人信息获取的接口
        if (null == userInfo) {

            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
            String resultUserInfo = new String(HttpUtil.doGet(userInfoUrl));
            log.info("获取到的用户信息: {}", resultUserInfo);
            JSONObject userInfoObj = JSON.parseObject(resultUserInfo);
            if (userInfoObj.getString("errcode") != null) {
                log.error("获取用户信息失败: {}", userInfoObj.getString("errmsg"));
            }
            // 解析用户信息
            String nickname = userInfoObj.getString("nickname");
            //String headimgurl = userInfoObj.getString("headimgurl");

            // 存数据库
            userInfo = new UserInfo();
            userInfo.setOpenid(openid);
            userInfo.setNickName(nickname);
            userInfo.setStatus(1);
            userInfoService.save(userInfo);
        }

        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if (StrUtil.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StrUtil.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);
        if (StrUtil.isEmpty(userInfo.getPhone())) {
            map.put("openid", userInfo.getOpenid());
        } else {
            map.put("openid", "");
        }
        String token = JwtHelper.createToken(String.valueOf(userInfo.getId()), name);
        map.put("token", token);
        return "redirect:" + wxConfigProperties.getYyghBaseUrl() + "/weixin/callback?token=" + map.get("token") + "&openid=" + map.get("openid") + "&name=" + URLEncoder.encode((String) map.get("name"), StandardCharsets.UTF_8);
    }
}
