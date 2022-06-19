package org.xiaowu.behappy.api.cmn.feign.factory;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.api.cmn.feign.DictFeign;
import org.xiaowu.behappy.api.cmn.feign.fallback.DictFeignFallBack;

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
