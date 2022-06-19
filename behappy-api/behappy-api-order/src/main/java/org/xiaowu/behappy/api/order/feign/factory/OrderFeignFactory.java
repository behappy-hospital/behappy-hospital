package org.xiaowu.behappy.api.order.feign.factory;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.api.order.feign.OrderFeign;
import org.xiaowu.behappy.api.order.feign.fallback.OrderFeignFallBack;

/**
 * @author xiaowu
 */
@Component
public class OrderFeignFactory implements FallbackFactory<OrderFeign> {

    @Override
    public OrderFeign create(Throwable cause) {
        OrderFeignFallBack dictFeignFallBack = new OrderFeignFallBack();
        dictFeignFallBack.setCause(cause);
        return dictFeignFallBack;
    }
}
