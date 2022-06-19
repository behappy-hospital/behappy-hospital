package org.xiaowu.behappy.api.hosp.feign.fallback;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.api.common.vo.SignInfoVo;
import org.xiaowu.behappy.api.hosp.feign.HospitalFeign;
import org.xiaowu.behappy.api.hosp.vo.ScheduleOrderVo;
import org.xiaowu.behappy.common.core.result.Result;

/**
 * @author xiaowu
 */
@Slf4j
@Component
public class HospitalFeignFallBack implements HospitalFeign {

    @Setter
    Throwable cause;

    @Override
    public Result<ScheduleOrderVo> getScheduleOrderVo(String scheduleId) {
        return Result.failed(cause);
    }

    @Override
    public Result<SignInfoVo> getSignInfoVo(String hoscode) {
        return Result.failed(cause);
    }
}
