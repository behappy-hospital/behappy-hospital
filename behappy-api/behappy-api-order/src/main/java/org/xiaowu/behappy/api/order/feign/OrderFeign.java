package org.xiaowu.behappy.api.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.xiaowu.behappy.api.order.feign.factory.OrderFeignFactory;
import org.xiaowu.behappy.api.order.vo.OrderCountQueryVo;
import org.xiaowu.behappy.common.core.result.Result;

import java.util.Map;

import static org.xiaowu.behappy.common.core.constants.ServiceConstants.ORDER_SERVICE;

/**
 * @author xiaowu
 */
@FeignClient(value = ORDER_SERVICE,
        path = "/api/order",
        fallbackFactory = OrderFeignFactory.class)
public interface OrderFeign {

    /**
     * 获取订单统计数据
     */
    @PostMapping("/orderInfo/inner/getCountMap")
    Result<Map<String, Object>> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo);

}
