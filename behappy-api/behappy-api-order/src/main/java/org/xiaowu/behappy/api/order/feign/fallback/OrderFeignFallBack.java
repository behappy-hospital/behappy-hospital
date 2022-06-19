package org.xiaowu.behappy.api.order.feign.fallback;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.api.order.feign.OrderFeign;
import org.xiaowu.behappy.api.order.vo.OrderCountQueryVo;
import org.xiaowu.behappy.common.core.result.Result;

import java.util.Map;

/**
 * @author xiaowu
 */
@Slf4j
@Component
public class OrderFeignFallBack implements OrderFeign {

    @Setter
    Throwable cause;

    @Override
    public Result<Map<String, Object>> getCountMap(OrderCountQueryVo orderCountQueryVo) {
        return Result.failed(cause);
    }
}
