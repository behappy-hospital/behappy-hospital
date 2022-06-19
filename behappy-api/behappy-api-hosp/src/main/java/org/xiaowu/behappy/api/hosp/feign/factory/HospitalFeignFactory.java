package org.xiaowu.behappy.api.hosp.feign.factory;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.api.hosp.feign.HospitalFeign;
import org.xiaowu.behappy.api.hosp.feign.fallback.HospitalFeignFallBack;

/**
 * @author xiaowu
 */
@Component
public class HospitalFeignFactory implements FallbackFactory<HospitalFeign> {

    @Override
    public HospitalFeign create(Throwable cause) {
        HospitalFeignFallBack dictFeignFallBack = new HospitalFeignFallBack();
        dictFeignFallBack.setCause(cause);
        return dictFeignFallBack;
    }
}
