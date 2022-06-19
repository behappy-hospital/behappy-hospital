package org.xiaowu.behappy.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.xiaowu.behappy.api.order.enums.OrderStatusEnum;
import org.xiaowu.behappy.api.order.model.OrderInfo;
import org.xiaowu.behappy.api.order.vo.OrderCountQueryVo;
import org.xiaowu.behappy.api.order.vo.OrderQueryVo;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.order.service.OrderInfoService;
import org.xiaowu.behappy.order.service.OrderService;

import java.util.Map;

@Api(tags = "订单接口")
@RestController
@RequestMapping("/admin/order/orderInfo")
@RequiredArgsConstructor
public class OrderController {

    private final OrderInfoService orderInfoService;

    private final OrderService orderService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("/{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "orderCountQueryVo", value = "查询对象", required = false) OrderQueryVo orderQueryVo) {
        Page<OrderInfo> pageParam = new Page<>(page, limit);
        IPage<OrderInfo> pageModel = orderInfoService.selectPage(pageParam, orderQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取订单状态")
    @GetMapping("/getStatusList")
    public Result getStatusList() {
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    @ApiOperation(value = "获取订单")
    @GetMapping("show/{id}")
    public Result<Map<String, Object>> get(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @PathVariable Long id) {
        return Result.ok(orderInfoService.show(id));
    }

    @ApiOperation(value = "获取订单统计数据")
    @PostMapping("inner/getCountMap")
    public Result<Map<String, Object>> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo) {
        Map<String, Object> countMap = orderService.getCountMap(orderCountQueryVo);
        return Result.ok(countMap);
    }

}

