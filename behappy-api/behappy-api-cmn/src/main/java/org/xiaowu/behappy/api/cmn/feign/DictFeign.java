package org.xiaowu.behappy.api.cmn.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xiaowu.behappy.api.cmn.feign.factory.DictFeignFactory;
import org.xiaowu.behappy.common.core.result.Result;

import static org.xiaowu.behappy.common.core.constants.ServiceConstants.CMN_SERVICE;

/**
 * @author xiaowu
 */
@FeignClient(value = CMN_SERVICE,
        path = "/admin/cmn/dict",
        fallbackFactory = DictFeignFactory.class)
public interface DictFeign {

    @GetMapping("/getName")
    Result<String> getName(@RequestParam(value = "parentDictCode") String parentDictCode,
                           @RequestParam(value = "value") String value);
}
