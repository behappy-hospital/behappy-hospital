package org.xiaowu.behappy.common.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xiaowu.behappy.common.core.result.Result;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author 小五
 */
@Slf4j
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice(basePackages = "org.xiaowu.behappy")
public class ServletExceptionHandler {

    @ExceptionHandler(HospitalException.class)
    public Result beHappyExceptionHandler(HospitalException ex) {
        extractedErrPrint(ex);
        return Result.failed(ex.getCode(), ex.getMessage());
    }


    /**
     * 参数校验不通过异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        extractedErrPrint(ex);
        StringJoiner sj = new StringJoiner(";");
        ex.getBindingResult().getFieldErrors().forEach(x -> sj.add(x.getDefaultMessage()));
        return Result.failed(ex);
    }

    /**
     * Controller参数绑定错误
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        extractedErrPrint(ex);
        return Result.failed(ex);
    }


    /**
     * 其他错误
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception ex) {
        extractedErrPrint(ex);
        return Result.failed(ex);
    }

    private static void extractedErrPrint(Exception ex) {
        if (Objects.nonNull(ex.getCause())) {
            log.error("全局异常捕获", ex.getCause());
        }
        if (ex.getStackTrace().length>0) {
            StackTraceElement stackTraceElement = ex.getStackTrace()[0];
            log.error(
                    new StringJoiner(" - ")
                            .add(stackTraceElement.getClassName())
                            .add(stackTraceElement.getMethodName())
                            .add(String.valueOf(stackTraceElement.getLineNumber()))
                            .add(ex.getMessage()).toString());
        }else {
            log.error(ex.getMessage());
        }
    }
}
