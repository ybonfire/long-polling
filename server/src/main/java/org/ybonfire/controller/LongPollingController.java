package org.ybonfire.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ybonfire.model.request.AddListenerRequest;
import org.ybonfire.service.ConfigDataManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LongPollingController
 *
 * @author yuanbo
 * @date 2023-09-15 14:29
 */
@RequestMapping(value = "/apis/listener")
@RestController
public class LongPollingController {
    @Autowired
    private ConfigDataManager configDataManager;

    /**
     * @description: 添加监听者
     * @param:
     * @return:
     * @date: 2023/09/15 14:36:59
     */
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addListener(@Validated @RequestBody final AddListenerRequest request, final HttpServletRequest req,
        final HttpServletResponse resp) {
        configDataManager.addListener(request, req, resp);
    }
}
