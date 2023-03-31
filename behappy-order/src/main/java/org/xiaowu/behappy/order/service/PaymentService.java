package org.xiaowu.behappy.order.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiaowu.behappy.api.common.vo.SignInfoVo;
import org.xiaowu.behappy.api.hosp.feign.HospitalFeign;
import org.xiaowu.behappy.api.order.enums.OrderStatusEnum;
import org.xiaowu.behappy.api.order.enums.PaymentStatusEnum;
import org.xiaowu.behappy.common.core.exception.HospitalException;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.common.core.result.ResultCodeEnum;
import org.xiaowu.behappy.common.core.util.HttpRequestHelper;
import org.xiaowu.behappy.common.core.util.ResponseConvert;
import org.xiaowu.behappy.order.entity.OrderInfo;
import org.xiaowu.behappy.order.entity.PaymentInfo;
import org.xiaowu.behappy.order.mapper.PaymentInfoMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xiaowu
 */
@Service
@AllArgsConstructor
public class PaymentService extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements IService<PaymentInfo> {

    private final OrderInfoService orderInfoService;

    private final HospitalFeign hospitalFeign;

    private final ResponseConvert responseConvert;

    /**
     * 保存交易记录
     * @param orderInfo
     * @param paymentType 支付类型（1：微信 2：支付宝）
     */
    public void savePaymentInfo(OrderInfo orderInfo, Integer paymentType) {
        QueryWrapper<PaymentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderInfo.getId());
        queryWrapper.eq("payment_type", paymentType);
        Long count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            return;
        }
        // 保存交易记录
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderId(orderInfo.getId());
        paymentInfo.setPaymentType(paymentType);
        paymentInfo.setOutTradeNo(orderInfo.getOutTradeNo());
        paymentInfo.setPaymentStatus(PaymentStatusEnum.UNPAID.getStatus());
        String subject = new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd") + "|" + orderInfo.getHosname() + "|" + orderInfo.getDepname() + "|" + orderInfo.getTitle();
        paymentInfo.setSubject(subject);
        paymentInfo.setTotalAmount(orderInfo.getAmount());
        baseMapper.insert(paymentInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(String outTradeNo, Integer paymentType, Map<String, String> paramMap) {
        PaymentInfo paymentInfo = this.getPaymentInfo(outTradeNo, paymentType);
        if (null == paymentInfo) {
            throw new HospitalException(ResultCodeEnum.PARAM_ERROR);
        }
        if (!paymentInfo.getPaymentStatus().equals(PaymentStatusEnum.UNPAID.getStatus())) {
            return;
        }
        //修改支付状态
        PaymentInfo paymentInfoUpd = new PaymentInfo();
        paymentInfoUpd.setPaymentStatus(PaymentStatusEnum.PAID.getStatus());
        paymentInfoUpd.setTradeNo(paramMap.get("transaction_id"));
        paymentInfoUpd.setCallbackTime(new Date());
        paymentInfoUpd.setCallbackContent(paramMap.toString());
        this.updatePaymentInfo(outTradeNo, paymentInfoUpd);
        //修改订单状态
        OrderInfo orderInfo = orderInfoService.getById(paymentInfo.getOrderId());
        orderInfo.setOrderStatus(OrderStatusEnum.PAID.getStatus());
        orderInfoService.updateById(orderInfo);
        // 调用医院接口，通知更新支付状态
        Result<SignInfoVo> signInfoVoResult = hospitalFeign.getSignInfoVo(orderInfo.getHoscode());
        SignInfoVo signInfoVo = responseConvert.convert(signInfoVoResult, new TypeReference<SignInfoVo>() {
        });
        if (null == signInfoVo) {
            throw new HospitalException(ResultCodeEnum.PARAM_ERROR);
        }
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("hoscode", orderInfo.getHoscode());
        reqMap.put("hosRecordId", orderInfo.getHosRecordId());
        reqMap.put("timestamp", HttpRequestHelper.getTimestamp());
        String sign = HttpRequestHelper.getSign(reqMap, signInfoVo.getSignKey());
        reqMap.put("sign", sign);
        JSONObject result = HttpRequestHelper.sendRequest(reqMap, signInfoVo.getApiUrl() + "/order/updatePayStatus");
        if (result.getInteger("code") != 200) {
            throw new HospitalException(result.getString("message"), ResultCodeEnum.FAIL.getCode());
        }
    }

    private PaymentInfo getPaymentInfo(String outTradeNo, Integer paymentType) {
        LambdaQueryWrapper<PaymentInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PaymentInfo::getOutTradeNo, outTradeNo);
        lambdaQueryWrapper.eq(PaymentInfo::getPaymentType, paymentType);
        return this.getOne(lambdaQueryWrapper);
    }

    public PaymentInfo getPaymentInfo(Long orderId, Integer paymentType) {
        QueryWrapper<PaymentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        queryWrapper.eq("payment_type", paymentType);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 更改支付记录
     */
    private void updatePaymentInfo(String outTradeNo, PaymentInfo paymentInfoUpd) {
        QueryWrapper<PaymentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("out_trade_no", outTradeNo);
        baseMapper.update(paymentInfoUpd, queryWrapper);
    }

}
