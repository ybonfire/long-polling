package org.ybonfire.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 添加监听者请求
 *
 * @author yuanbo
 * @date 2023-09-15 14:32
 */
@Data
public class AddListenerRequest {
    /**
     * 监听的数据id
     */
    @NotNull(message = "dataId must not be null")
    private String dataId;
    /**
     * 监听者id
     */
    @NotNull(message = "listenerId must not be null")
    private String listenerId;
    /**
     * 超时时长
     */
    private Long timeoutMillis;
}
