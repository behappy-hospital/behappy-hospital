package org.xiaowu.behappy.api.user.feign.factory;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.api.user.feign.PatientFeign;
import org.xiaowu.behappy.api.user.feign.fallback.PatientFeignFallBack;

/**
 * @author xiaowu
 */
@Component
public class PatientFeignFactory implements FallbackFactory<PatientFeign> {

    @Override
    public PatientFeign create(Throwable cause) {
        PatientFeignFallBack dictFeignFallBack = new PatientFeignFallBack();
        dictFeignFallBack.setCause(cause);
        return dictFeignFallBack;
    }
}
