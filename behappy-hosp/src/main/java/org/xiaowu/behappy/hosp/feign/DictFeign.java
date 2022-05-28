package org.xiaowu.behappy.hosp.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xiaowu.behappy.common.core.result.Response;
import org.xiaowu.behappy.hosp.feign.factory.DictFeignFactory;

import static org.xiaowu.behappy.common.core.constants.ServiceConstants.CMN_SERVICE;

/**
 * @author xiaowu
 */
@FeignClient(value = CMN_SERVICE,fallbackFactory = DictFeignFactory.class)
public interface DictFeign {

    @GetMapping("/admin/cmn/dict/getName")
    Response<String> getName(@RequestParam(value = "parentDictCode") String parentDictCode,
                             @RequestParam(value = "value") String value);
}
