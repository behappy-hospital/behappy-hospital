package org.xiaowu.behappy.order.service;

import com.alibaba.fastjson2.JSONObject;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import io.seata.rm.tcc.api.LocalTCC;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.api.order.enums.PaymentTypeEnum;
import org.xiaowu.behappy.api.order.enums.RefundStatusEnum;
import org.xiaowu.behappy.common.core.util.IpUtil;
import org.xiaowu.behappy.order.config.WxConfigProperties;
import org.xiaowu.behappy.order.entity.OrderInfo;
import org.xiaowu.behappy.order.entity.PaymentInfo;
import org.xiaowu.behappy.order.entity.RefundInfo;
import org.xiaowu.behappy.order.util.HttpClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class WeixinService {

    private final OrderInfoService orderInfoService;

    private final PaymentService paymentService;

    private final RedisTemplate<String, Object> redisTemplate;

    private final WxConfigProperties wxConfigProperties;

    private final RefundInfoService refundInfoService;

    private final HttpServletRequest httpServletRequest;

    /**
     * 根据订单号下单，生成支付链接
     */
    public Map<String, Object> createNative(Long orderId) {
        try {
            Map<String, Object> payMap = (Map<String, Object>) redisTemplate.opsForValue().get(orderId.toString());
            if (null != payMap) {
                return payMap;
            }
            //根据id获取订单信息
            OrderInfo order = orderInfoService.getById(orderId);
            // 保存交易记录
            paymentService.savePaymentInfo(order, PaymentTypeEnum.WEIXIN.getStatus());
            //1、设置参数
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("appid", wxConfigProperties.getAppId());
            paramMap.put("mch_id", wxConfigProperties.getPartner());
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            String body = order.getReserveDate() + "就诊" + order.getDepname();
            paramMap.put("body", body);
            paramMap.put("out_trade_no", order.getOutTradeNo());
            // 单位是分
            paramMap.put("total_fee", order.getAmount()
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(0, RoundingMode.CEILING)
                    .toString());
            //paramMap.put("total_fee", "1");
            paramMap.put("spbill_create_ip", IpUtil.getIpAddr(httpServletRequest));
            paramMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
            paramMap.put("trade_type", "NATIVE");
            //2、HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, wxConfigProperties.getPartnerKey()));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            log.error("微信支付码返回信息: {}", resultMap.get("return_msg"));
            //4、封装返回结果集
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("totalFee", order.getAmount());
            map.put("resultCode", resultMap.get("result_code"));
            map.put("codeUrl", resultMap.get("code_url"));
            if (null != resultMap.get("result_code")) {
                //微信支付二维码2小时过期，可采取2小时未支付取消订单
                redisTemplate.opsForValue().set(orderId.toString(), map, 1000, TimeUnit.MINUTES);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public Map<String, String> queryPayStatus(Long orderId, String paymentType) {
        try {
            OrderInfo orderInfo = orderInfoService.getById(orderId);
            //1、封装参数
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("appid", wxConfigProperties.getAppId());
            paramMap.put("mch_id", wxConfigProperties.getPartner());
            paramMap.put("out_trade_no", orderInfo.getOutTradeNo());
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, wxConfigProperties.getPartnerKey()));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据，转成Map
            String xml = client.getContent();
            //4、返回
            return WXPayUtil.xmlToMap(xml);
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean refund(Long orderId) {
        try {
            PaymentInfo paymentInfoQuery = paymentService.getPaymentInfo(orderId, PaymentTypeEnum.WEIXIN.getStatus());

            RefundInfo refundInfo = refundInfoService.saveRefundInfo(paymentInfoQuery);
            if (refundInfo.getRefundStatus().equals(RefundStatusEnum.REFUND.getStatus())) {
                return true;
            }
            Map<String, String> paramMap = new HashMap<>(8);
            paramMap.put("appid", wxConfigProperties.getAppId());       //公众账号ID
            paramMap.put("mch_id", wxConfigProperties.getPartner());   //商户编号
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            paramMap.put("transaction_id", paymentInfoQuery.getTradeNo()); //微信订单号
            paramMap.put("out_trade_no", paymentInfoQuery.getOutTradeNo()); //商户订单编号
            paramMap.put("out_refund_no", "tk" + paymentInfoQuery.getOutTradeNo()); //商户退款单号
            // 单位是分
            paramMap.put("total_fee", paymentInfoQuery.getTotalAmount()
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(0, RoundingMode.CEILING)
                    .toString());
            //paramMap.put("total_fee", "1");
            paramMap.put("refund_fee", paymentInfoQuery.getTotalAmount()
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(0, RoundingMode.CEILING)
                    .toString());
            //paramMap.put("refund_fee", "1");
            String paramXml = WXPayUtil.generateSignedXml(paramMap, wxConfigProperties.getPartnerKey());
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/secapi/pay/refund");
            client.setXmlParam(paramXml);
            client.setHttps(true);
            client.setCert(true);
            client.setCertPassword(wxConfigProperties.getPartner());
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            if (WXPayConstants.SUCCESS.equalsIgnoreCase(resultMap.get("result_code"))) {
                refundInfo.setCallbackTime(new Date());
                refundInfo.setTradeNo(resultMap.get("refund_id"));
                refundInfo.setRefundStatus(RefundStatusEnum.REFUND.getStatus());
                refundInfo.setCallbackContent(JSONObject.toJSONString(resultMap));
                refundInfoService.updateById(refundInfo);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
