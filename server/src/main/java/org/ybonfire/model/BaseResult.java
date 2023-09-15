package org.ybonfire.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.UUID;

/**
 * @description: BaseResult
 * @author: yuanbo
 * @date: 2023/9/15
 */
@Slf4j
@Data
public class BaseResult<T> implements Serializable {
    /**
     * 成功
     */
    public static final int SUCCESS = 1000000;

    private static final long serialVersionUID = 1L;

    private int code;

    private String message;

    private T data;

    private String[] errorArgs;

    private Long currentTime;

    private String traceId;

    public static BaseResult ok() {
        return restBaseResult(null, SUCCESS, "success", null);
    }

    public static <T> BaseResult<T> ok(T data) {
        return restBaseResult(data, SUCCESS, "success", null);
    }

    public static <T> BaseResult<T> ok(T data, String message) {
        return restBaseResult(data, SUCCESS, message, null);
    }

    public static BaseResult fail(int code, String message) {
        return restBaseResult(null, code, message, null);
    }

    public static <T> BaseResult<T> fail(int code, String message, T data) {
        return restBaseResult(data, code, message, null);
    }

    public static BaseResult fail(int code, String message, String[] errors) {
        return restBaseResult(null, code, message, errors);
    }

    private static <T> BaseResult<T> restBaseResult(T data, int code, String message, String[] errors) {
        BaseResult<T> apiBaseResult = new BaseResult<>();
        apiBaseResult.setCode(code);
        apiBaseResult.setData(data);
        apiBaseResult.setErrorArgs(errors);
        apiBaseResult.setMessage(message);
        apiBaseResult.setCurrentTime(System.currentTimeMillis());
        apiBaseResult.setTraceId(UUID.randomUUID().toString());
        return apiBaseResult;
    }

    public boolean isSuccess() {
        return SUCCESS == this.code;
    }
}
