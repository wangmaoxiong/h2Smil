package com.wmx.controller;

import com.wmx.config.AppBasicConfigProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/9/5 16:36
 */
@RestController
public class AppController {

    @Resource
    private AppBasicConfigProperties appBasicConfigProperties;

    /**
     * http:localhost:8080/app/getInfo
     *
     * @return
     */
    @GetMapping("app/getInfo")
    public Map<String, Object> getInfo() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("author", appBasicConfigProperties.getAppAuthor());
        dataMap.put("version", appBasicConfigProperties.getAppVersion());
        dataMap.put("launchDate", appBasicConfigProperties.getLaunchDate());
        dataMap.put("info", appBasicConfigProperties.getAppInfo());
        return dataMap;
    }
}
