package com.wmx.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wangmaoxiong
 * @version 1.0
 * @date 2020/6/7 20:13
 */
public class AppUtils {

    /**
     * 如果 source 为空，或者是非法数字，则使用默认值 defaultTarget。
     * isNumeric 非法对负数("-100")也是返回 false
     *
     * @param source
     * @param defaultTarget
     * @return
     */
    public static String checkNumeric(String source, String defaultTarget) {
        source = source == null ? defaultTarget : StringUtils.isNumeric(source) ? source : defaultTarget;
        return source;
    }
}
