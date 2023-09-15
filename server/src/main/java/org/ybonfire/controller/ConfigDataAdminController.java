package org.ybonfire.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ybonfire.model.BaseResult;
import org.ybonfire.model.ConfigData;
import org.ybonfire.service.ConfigDataManager;

/**
 * ConfigDataAdminController
 *
 * @author yuanbo
 * @date 2023-09-15 17:17
 */
@RequestMapping(value = "/apis/config")
@RestController
public class ConfigDataAdminController {
    @Autowired
    private ConfigDataManager configDataManager;

    /**
     * @description: 添加配置
     * @param:
     * @return:
     * @date: 2023/09/15 17:25:15
     */
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addConfigData(@RequestBody final ConfigData data) {
        configDataManager.add(data);
        return BaseResult.ok();
    }

    /**
     * @description: 删除配置
     * @param:
     * @return:
     * @date: 2023/09/15 17:25:15
     */
    @DeleteMapping("/remove/{id}")
    public BaseResult removeConfigData(@PathVariable String id) {
        configDataManager.remove(id);
        return BaseResult.ok();
    }
}
