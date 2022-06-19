package org.xiaowu.behappy.common.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.common.core.exception.HospitalException;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.common.core.result.ResultCodeEnum;

/**
 * @author 小五
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseConvert {

    private final ObjectMapper objectMapper;

    private static void assertResponse(Result result) {
        try {
            assert result.getCode() == 0;
        } catch (Exception e) {
            log.error("assert response error -> code:{},msg:{}", result.getCode(), result.getMessage());
            throw new HospitalException(result.getMessage(), result.getCode());
        }
    }

    public <T> T convert(Result result, TypeReference<T> type) {
        try {
            ResponseConvert.assertResponse(result);
            return objectMapper.readValue(objectMapper.writeValueAsString(result.getData()), type);
        } catch (JsonProcessingException e) {
            log.error("Response转换：序列化错误");
            throw new HospitalException(e.getMessage(), ResultCodeEnum.FAIL.getCode());
        } catch (HospitalException e) {
            throw e;
        } catch (Exception e) {
            log.error("Response转换：未知错误");
            throw e;
        }
    }
}
