package org.xiaowu.behappy.api.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.xiaowu.behappy.api.user.feign.factory.PatientFeignFactory;
import org.xiaowu.behappy.api.user.vo.PatientVo;
import org.xiaowu.behappy.common.core.result.Result;

import static org.xiaowu.behappy.common.core.constants.ServiceConstants.USER_SERVICE;

@FeignClient(value = USER_SERVICE,
        path = "/api/user/patient",
        fallbackFactory = PatientFeignFactory.class)
public interface PatientFeign {
    //获取就诊人
    @GetMapping("/inner/get/{id}")
    Result<PatientVo> getPatient(@PathVariable("id") Long id);
}
