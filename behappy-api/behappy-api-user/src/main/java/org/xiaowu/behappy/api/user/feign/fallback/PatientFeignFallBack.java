package org.xiaowu.behappy.api.user.feign.fallback;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.api.user.feign.PatientFeign;
import org.xiaowu.behappy.api.user.vo.PatientVo;
import org.xiaowu.behappy.common.core.result.Result;

/**
 * @author xiaowu
 */
@Slf4j
@Component
public class PatientFeignFallBack implements PatientFeign {

    @Setter
    Throwable cause;

    @Override
    public Result<PatientVo> getPatient(Long id) {
        log.error("PatientFeignFallBack - getPatient: {}", id);
        return Result.failed();
    }
}
