package org.xiaowu.behappy.api.cmn.feign.fallback;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.api.cmn.feign.DictFeign;
import org.xiaowu.behappy.common.core.result.Result;

/**
 * @author xiaowu
 */
@Slf4j
@Component
public class DictFeignFallBack implements DictFeign {

    @Setter
    Throwable cause;

    @Override
    public Result<String> getName(String parentDictCode, String value) {
        log.error("DictFeignFallBack - getName: {}-{}", parentDictCode,value);
        return Result.failed(cause);
    }
}
