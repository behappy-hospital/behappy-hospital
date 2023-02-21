package org.xiaowu.behappy.order.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaowu.behappy.api.order.enums.PaymentTypeEnum;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.order.service.PaymentService;
import org.xiaowu.behappy.order.service.WeixinService;

import java.util.Map;

@RestController
@RequestMapping("/api/order/weixin")
@RequiredArgsConstructor
public class WeixinController {
    private final WeixinService weixinPayService;

    private final PaymentService paymentService;
    /**
     * 下单 生成二维码
     */
    @GetMapping("/createNative/{orderId}")
    public Result<Map> createNative(
            @Parameter(name = "orderId", description = "订单id", required = true)
            @PathVariable("orderId") Long orderId) {
        return Result.ok(weixinPayService.createNative(orderId));
    }

    @Operation(summary = "查询支付状态")
    @GetMapping("/queryPayStatus/{orderId}")
    public Result<Object> queryPayStatus(
            @Parameter(name = "orderId", description = "订单id", required = true)
            @PathVariable("orderId") Long orderId) {
        //调用查询接口
        Map<String, String> resultMap = weixinPayService.queryPayStatus(orderId, PaymentTypeEnum.WEIXIN.name());
        if (resultMap == null) {//出错
            return Result.failed("支付出错");
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))) {//如果成功
            //更改订单状态，处理支付结果
            String outTradeNo = resultMap.get("out_trade_no");
            paymentService.paySuccess(outTradeNo, PaymentTypeEnum.WEIXIN.getStatus(), resultMap);
            return Result.ok();
        }
        return Result.ok().setMessage("支付中");
    }

}
