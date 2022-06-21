package org.xiaowu.behappy.order.controller.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.xiaowu.behappy.api.order.enums.OrderStatusEnum;
import org.xiaowu.behappy.api.order.vo.OrderQueryVo;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.common.core.util.AuthContextHolder;
import org.xiaowu.behappy.order.entity.OrderInfo;
import org.xiaowu.behappy.order.service.OrderInfoService;
import org.xiaowu.behappy.order.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "订单接口")
@RestController
@RequestMapping("/api/order/orderInfo")
@AllArgsConstructor
public class OrderApiController {

    private final OrderService orderService;

    private final OrderInfoService orderInfoService;

    @ApiOperation(value = "创建订单")
    @PostMapping("/auth/submitOrder/{scheduleId}/{patientId}")
    public Result<Long> submitOrder(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable String scheduleId,
            @ApiParam(name = "patientId", value = "就诊人id", required = true)
            @PathVariable Long patientId) {
        return Result.ok(orderService.saveOrder(scheduleId, patientId));
    }

    //订单列表（条件查询带分页）
    @GetMapping("/auth/{page}/{limit}")
    public Result<IPage<OrderInfo>> list(@PathVariable Long page,
                       @PathVariable Long limit,
                       OrderQueryVo orderQueryVo, HttpServletRequest request) {
        //设置当前用户id
        orderQueryVo.setUserId(AuthContextHolder.getUserId(request));
        Page<OrderInfo> pageParam = new Page<>(page,limit);
        IPage<OrderInfo> pageModel =
                orderInfoService.selectPage(pageParam,orderQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取订单状态")
    @GetMapping("/auth/getStatusList")
    public Result<List<Map<String, Object>>> getStatusList() {
        List<Map<String, Object>> statusList = OrderStatusEnum.getStatusList();
        return Result.ok(statusList);
    }

    //根据订单id查询订单详情
    @GetMapping("/auth/getOrders/{orderId}")
    public Result<OrderInfo> getOrders(@PathVariable String orderId) {
        OrderInfo orderInfo = orderInfoService.getById(orderId);
        return Result.ok(orderInfo);
    }

    @ApiOperation(value = "取消预约")
    @GetMapping("/auth/cancelOrder/{orderId}")
    public Result<Boolean> cancelOrder(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @PathVariable("orderId") Long orderId) {
        return Result.ok(orderService.cancelOrder(orderId));
    }


}
