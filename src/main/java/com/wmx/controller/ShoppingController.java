package com.wmx.controller;

import com.wmx.service.ShortMessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author wangmaoxiong
 * @version 1.0
 * @date 2020/5/27 16:53
 */
@RestController
public class ShoppingController {
    @Resource
    private ShortMessageService shortMessageService;

    /**
     * 短信发送对外接口
     * http://localhost:8080/shopping/buyGoods?toUser=10086&content=您购买的商品支付成功，商家马上给你发货！
     * http://localhost:8080/shopping/buyGoods?toUser=10010&content=您购买的商品支付成功，商家即将给你发货！
     *
     * @param toUser
     * @param content
     * @return
     */
    @GetMapping("/shopping/buyGoods")
    public String buyGoods(@RequestParam String toUser, @RequestParam String content) {
        shortMessageService.sendMessage(toUser, content);
        shortMessageService.sendMessage("95533", content);
        return "success " + new Date();
    }
}
