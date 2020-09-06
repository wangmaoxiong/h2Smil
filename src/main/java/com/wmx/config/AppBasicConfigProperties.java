package com.wmx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * ==@Validated 注解校验配置文件属性值==
 * 1、@NotEmpty、@Email 等等注解必须与 @Validated 同时使用才会生效
 * ==@ConfigurationProperties(prefix = "app.basic")：表示匹配 app.basic 层级下的属性
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/9/5 16:16
 */
@Validated
@ConfigurationProperties(prefix = "app.basic")
@Component
public class AppBasicConfigProperties {
    /**
     * 需要注入值的属性必须提供 setter 方法
     */
    private String appAuthor;
    private String appVersion;
    private Date launchDate;
    @NotEmpty(message = "【app.basic.appInfo】属性配置不能为空！")
    private String appInfo;

    public String getAppAuthor() {
        return appAuthor;
    }

    public void setAppAuthor(String appAuthor) {
        this.appAuthor = appAuthor;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public String getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(String appInfo) {
        this.appInfo = appInfo;
    }
}
