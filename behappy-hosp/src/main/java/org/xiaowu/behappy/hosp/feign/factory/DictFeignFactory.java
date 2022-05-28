package org.xiaowu.behappy.hosp.feign.factory;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.hosp.feign.DictFeign;
import org.xiaowu.behappy.hosp.feign.fallback.DictFeignFallBack;

/**
 * @author xiaowu
 */
@Component
public class DictFeignFactory implements FallbackFactory<DictFeign> {

    @Override
    public DictFeign create(Throwable cause) {
        DictFeignFallBack dictFeignFallBack = new DictFeignFallBack();
        dictFeignFallBack.setCause(cause);
        return dictFeignFallBack;
    }
}
