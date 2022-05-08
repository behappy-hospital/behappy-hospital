package org.xiaowu.behappy.common.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xiaowu.behappy.common.core.result.Response;

/**
 * @author 小五
 */
@Slf4j
@RestControllerAdvice(basePackages = "org.xiaowu.behappy")
public class DefaultExceptionHandlerConfig {

    @ExceptionHandler(HospitalException.class)
    public Response beHappyExceptionHandler(HospitalException e) {
        log.error("DefaultExceptionHandlerConfig - beHappyExceptionHandler: {}", e.getMessage());
        return Response.failed(e.getCode(), e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public Response exceptionHandler(Exception e) {
        log.error("DefaultExceptionHandlerConfig - exceptionHandler: {}", e.getLocalizedMessage());
        return Response.failed(e);
    }
}