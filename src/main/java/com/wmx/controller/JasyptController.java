package com.wmx.controller;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wmx.utils.JasyptUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wangmaoxiong
 * @version 1.0
 * @date 2020/5/29 11:09
 */
@RestController
public class JasyptController {
    @Value("${author.infos.address}")
    private String authorAddress;
    @Value("${author.infos.email}")
    private String authorEmail;

    @Resource
    private StringEncryptor stringEncryptor;

    /**
     * http://localhost:8080/jasypt/encryptor?message=12日下午17点执行任务&isEncrypt=true
     * http://localhost:8080/jasypt/encryptor?message=702EAA3755766C567F62E83273681A90DC684B6AFADD5CD84691778DAF4A1466E13CE0720E8BABC06081A5D6DBD90EA1&isEncrypt=false
     * 在线使用 {@link StringEncryptor} 加解密消息。
     *
     * @param message   加/解密的内容
     * @param isEncrypt true 表示加密、false 表示解密
     * @return
     */
    @GetMapping("jasypt/encryptor")
    public ObjectNode encrypt(@RequestParam String message, @RequestParam boolean isEncrypt) {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        String encrypt = isEncrypt ? stringEncryptor.encrypt(message) : stringEncryptor.decrypt(message);
        ObjectNode objectNode = nodeFactory.objectNode();
        objectNode.put("code", 200);
        objectNode.put("data", encrypt);
        return objectNode;
    }

    /**
     * http://localhost:8080/jasypt/encrypt?secretKey=wangmx&message=修长城的民族&isEncrypt=true
     * 在线使用 {@link StringEncryptor} 加解密消息。
     *
     * @param secretKey ：密钥。加/解密必须使用同一个密钥
     * @param message   ：加/解密的内容
     * @param isEncrypt ：true 表示加密、false 表示解密
     * @return
     */
    @GetMapping("jasypt/encrypt")
    public ObjectNode jasyptEncrypt(@RequestParam String secretKey, @RequestParam String message, @RequestParam boolean isEncrypt) {
        String encryptor = JasyptUtils.stringEncryptor(secretKey, message, isEncrypt);
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        ObjectNode objectNode = nodeFactory.objectNode();
        objectNode.put("code", 200);
        objectNode.put("secretKey", secretKey);
        objectNode.put("message", message);
        objectNode.put("isEncrypt", isEncrypt);
        objectNode.put("data", encryptor);
        return objectNode;
    }

    /**
     * 获取属性值
     * http://localhost:8080/jasypt/get
     *
     * @return
     */
    @GetMapping("/jasypt/get")
    public ObjectNode getJasypt() {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        ObjectNode objectNode = nodeFactory.objectNode();
        objectNode.put("authorAddress", authorAddress);
        objectNode.put("authorEmail", authorEmail);
        return objectNode;
    }

}
