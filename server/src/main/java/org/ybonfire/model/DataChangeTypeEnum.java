package org.ybonfire.model;

/**
 * 数据变更类型枚举
 *
 * @author yuanbo
 * @date 2023-09-15 14:52
 */
public enum DataChangeTypeEnum {
    /**
     * 新增
     */
    ADD(1, "add"),
    /**
     * 更新
     */
    UPDATE(0, "update"),
    /**
     * 删除
     */
    DELETE(-1, "delete"),;

    private final int code;
    private final String description;

    DataChangeTypeEnum(final int code, final String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
