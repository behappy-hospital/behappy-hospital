package org.xiaowu.behappy.msm.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.api.common.vo.MsmVo;
import org.xiaowu.behappy.msm.config.AliSmsProperties;

/**
 * @author xiaowu
 */
@Slf4j
@Service
@AllArgsConstructor
public class MsmService {

    /**
     * 初始化ascClient需要的几个参数
     * 短信API产品名称（短信产品名固定，无需修改）
     */
    final String product = "Dysmsapi";

    private final AliSmsProperties aliSmsProperties;

    @SneakyThrows
    public boolean sendSms(String phone, String code) {

        ////设置超时时间-可自行调整
        //System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        //System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //
        ////替换成你的AK
        //final String accessKeyId = aliSmsProperties.getAccessKeyId();//你的accessKeyId,参考本文档步骤2
        //final String accessKeySecret = aliSmsProperties.getAccessKeySecret();//你的accessKeySecret，参考本文档步骤2
        //final String signName = aliSmsProperties.getSignName();//必填:短信签名-可在短信控制台中找到，参考本文档步骤2
        ////初始化ascClient,暂时不支持多region（请勿修改）
        //IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
        //        accessKeySecret);
        //DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product);
        //IAcsClient acsClient = new DefaultAcsClient(profile);
        ////组装请求对象
        //SendSmsRequest request = new SendSmsRequest();
        ////使用post提交
        //request.setSysMethod(MethodType.POST);
        ////必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
        //request.setPhoneNumbers(phone);
        ////必填:短信签名-可在短信控制台中找到
        //request.setSignName(signName);
        ////必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        //request.setTemplateCode(aliSmsProperties.getTemplateCode());
        ////可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        ////友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        ////参考：request.setTemplateParam("{\"变量1\":\"值1\",\"变量2\":\"值2\",\"变量3\":\"值3\"}")
        //request.setTemplateParam("{\"code\":" + code + "}");
        ////可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        ////request.setSmsUpExtendCode("90997");
        ////可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        ////request.setOutId("yourOutId");
        ////请求失败这里会抛ClientException异常
        //SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        //return sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK");
        // todo,模拟发送短信
        log.info("-----------------------------------------------------------");
        log.info("发送短信至 {}, 内容为: {}",phone,code);
        log.info("-----------------------------------------------------------");
        return true;
    }

    public boolean send(MsmVo msmVo) {
        if (StrUtil.isNotBlank(msmVo.getPhone())) {
            String code = JSONUtil.toJsonPrettyStr(msmVo.getParam());
            return this.sendSms(msmVo.getPhone(), code);
        }
        return false;
    }

}
