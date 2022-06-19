package org.xiaowu.behappy.common.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xiaowu.behappy.common.core.result.Result;

/**
 * @author 小五
 */
@Slf4j
@RestControllerAdvice(basePackages = "org.xiaowu.behappy")
public class DefaultExceptionHandlerConfig {

    @ExceptionHandler(HospitalException.class)
    public Result beHappyExceptionHandler(HospitalException e) {
        log.error("DefaultExceptionHandlerConfig - beHappyExceptionHandler: {}", e.getMessage());
        return Result.failed(e.getCode(), e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e) {
        log.error("DefaultExceptionHandlerConfig - exceptionHandler: {}", e.getLocalizedMessage());
        return Result.failed(e);
    }
}