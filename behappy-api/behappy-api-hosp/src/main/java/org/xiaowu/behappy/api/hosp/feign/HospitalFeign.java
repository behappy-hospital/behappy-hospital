package org.xiaowu.behappy.api.hosp.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.xiaowu.behappy.api.common.vo.SignInfoVo;
import org.xiaowu.behappy.api.hosp.feign.factory.HospitalFeignFactory;
import org.xiaowu.behappy.api.hosp.vo.ScheduleOrderVo;
import org.xiaowu.behappy.common.core.result.Result;

import static org.xiaowu.behappy.common.core.constants.ServiceConstants.HOSP_SERVICE;

/**
 * @author xiaowu
 */
@FeignClient(value = HOSP_SERVICE,
        path = "/api/hosp/hospital",
        fallbackFactory = HospitalFeignFactory.class)
public interface HospitalFeign {

    /**
     * 根据排班id获取预约下单数据
     */
    @GetMapping("/inner/getScheduleOrderVo/{scheduleId}")
    Result<ScheduleOrderVo> getScheduleOrderVo(@PathVariable("scheduleId") String scheduleId);
    /**
     * 获取医院签名信息
     */
    @GetMapping("/inner/getSignInfoVo/{hoscode}")
    Result<SignInfoVo> getSignInfoVo(@PathVariable("hoscode") String hoscode);

}
