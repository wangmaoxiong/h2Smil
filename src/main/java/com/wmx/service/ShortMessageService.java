package com.wmx.service;

/**
 * 短信业务层接口
 *
 * @author wangmaoxiong
 * @version 1.0
 * @date 2020/5/27 16:56
 */
public interface ShortMessageService {
    /**
     * 为指定用户发送短信
     *
     * @param toUser  ：接收短信的用户
     * @param content ：短信内容
     * @return
     */
    void sendMessage(String toUser, String content);
}
