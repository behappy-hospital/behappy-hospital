package org.xiaowu.behappy.statistics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaowu.behappy.api.order.feign.OrderFeign;
import org.xiaowu.behappy.api.order.vo.OrderCountQueryVo;
import org.xiaowu.behappy.common.core.result.Result;

import java.util.Map;

/**
 * @author 94391
 */
@Tag(name = "统计管理接口")
@RestController
@RequestMapping("/admin/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final OrderFeign orderFeign;

    @Operation(summary = "获取订单统计数据")
    @GetMapping("getCountMap")
    public Result<Map<String, Object>>  getCountMap(@Parameter(name = "orderCountQueryVo", description = "查询对象", required = false) OrderCountQueryVo orderCountQueryVo) {
        return orderFeign.getCountMap(orderCountQueryVo);
    }
}
