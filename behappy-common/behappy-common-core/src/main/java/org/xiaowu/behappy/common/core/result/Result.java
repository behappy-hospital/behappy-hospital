/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.xiaowu.behappy.common.core.result;


import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author xiaowu
 * 响应信息主体
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Slf4j
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * response返回码
     */
    @Setter
    @Getter
    private int code;

    /**
     * response返回信息(错误信息)
     */
    @Setter
    @Getter
    private String message;

    /**
     * response返回体
     */
    @Setter
    @Getter
    private T data;

    public static <T> Result<T> ok() {
        return Result.restResult(null, ResultCodeEnum.SUCCESS.getCode(), null);
    }

    public static <T> Result<T> ok(T data) {
        return Result.restResult(data, ResultCodeEnum.SUCCESS.getCode(), null);
    }

    public static <T> Result<T> ok(T data, String msg) {
        return Result.restResult(data, ResultCodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> failed() {
        return Result.restResult(null, ResultCodeEnum.FAIL.getCode(), null);
    }

    public static <T> Result<T> failed(String msg) {
        return Result.restResult(null, ResultCodeEnum.FAIL.getCode(), msg);
    }

    public static <T> Result<T> failed(T data) {
        return Result.restResult(data, ResultCodeEnum.FAIL.getCode(), null);
    }

    public static <T> Result<T> failed(T data, String msg) {
        return Result.restResult(data, ResultCodeEnum.FAIL.getCode(), msg);
    }


    public static <T> Result<T> failed(Throwable throwable) {
        return Result.restResult(null, ResultCodeEnum.FAIL.getCode(), throwable.getMessage());
    }

    /**
     * 未登录返回结果
     */
    public static <T> Result<T> unauthorized(T data) {
        return Result.restResult(null, ResultCodeEnum.FAIL.getCode(), "未认证");
    }

    /**
     * 未授权返回结果
     */
    public static <T> Result<T> forbidden(T data) {
        return Result.restResult(null, ResultCodeEnum.FAIL.getCode(), "未授权");
    }

    public static <T> Result<T> restResult(T data, int code, String msg) {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMessage(msg);
        return apiResult;
    }

}
