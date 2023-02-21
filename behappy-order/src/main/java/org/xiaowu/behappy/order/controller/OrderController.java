package org.xiaowu.behappy.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaowu.behappy.api.order.enums.OrderStatusEnum;
import org.xiaowu.behappy.api.order.vo.OrderQueryVo;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.order.entity.OrderInfo;
import org.xiaowu.behappy.order.service.OrderInfoService;
import org.xiaowu.behappy.order.service.OrderService;

import java.util.Map;

@Tag(name = "订单接口")
@RestController
@RequestMapping("/admin/order/orderInfo")
@RequiredArgsConstructor
public class OrderController {

    private final OrderInfoService orderInfoService;

    private final OrderService orderService;

    @Operation(summary = "获取分页列表")
    @GetMapping("/{page}/{limit}")
    public Result index(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @Parameter(name = "orderCountQueryVo", description = "查询对象", required = false) OrderQueryVo orderQueryVo) {
        Page<OrderInfo> pageParam = new Page<>(page, limit);
        IPage<OrderInfo> pageModel = orderInfoService.selectPage(pageParam, orderQueryVo);
        return Result.ok(pageModel);
    }

    @Operation(summary = "获取订单状态")
    @GetMapping("/getStatusList")
    public Result getStatusList() {
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    @Operation(summary = "获取订单")
    @GetMapping("/show/{id}")
    public Result<Map<String, Object>> get(
            @Parameter(name = "orderId", description = "订单id", required = true)
            @PathVariable Long id) {
        return Result.ok(orderInfoService.show(id));
    }


}

