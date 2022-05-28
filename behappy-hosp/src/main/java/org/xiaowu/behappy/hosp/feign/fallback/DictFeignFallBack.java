package org.xiaowu.behappy.hosp.feign.fallback;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.common.core.result.Response;
import org.xiaowu.behappy.hosp.feign.DictFeign;

/**
 * @author xiaowu
 */
@Slf4j
@Component
public class DictFeignFallBack implements DictFeign {

    @Setter
    Throwable cause;

    @Override
    public Response<String> getName(String parentDictCode, String value) {
        log.error("DictFeignFallBack - getName: {}-{}", parentDictCode,value);
        return Response.failed(cause);
    }
}
