package org.ybonfire.model;

import lombok.Data;

/**
 * 数据变更事件
 *
 * @author yuanbo
 * @date 2023-09-15 14:50
 */
@Data
public class ConfigDataChangeEvent {
    private final ConfigData data;
    private final DataChangeTypeEnum changeType;

    public ConfigDataChangeEvent(final ConfigData data, final DataChangeTypeEnum changeType) {
        this.data = data;
        this.changeType = changeType;
    }
}
