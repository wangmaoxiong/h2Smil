package com.wmx.service.impl;

import com.wmx.service.ShortMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.Future;

/**
 * 短信业务层实现
 *
 * @author wangmaoxiong
 * @version 1.0
 * @date 2020/5/27 16:56
 */
@Service
public class ShortMessageServiceImpl implements ShortMessageService {
    private static final Logger logger = LoggerFactory.getLogger(ShortMessageServiceImpl.class);

    /**
     * @param toUser  ：接收短信的用户
     * @param content ：短信内容
     * @return 为指定用户发送短信。异步方法
     * @Async 注解表示方法将异步执行，放在类上面时，整个类中的方法都将异步执行, value 属性指定线程池名称，不指定照样也是可以的。
     * 异步执行的方法的返回值需要注意，要么不返回，要么返回 {@link Future} 及其实现类.
     */
    @Override
    @Async
    public void sendMessage(String toUser, String content) {
        try {
            logger.info("发送短信开始：toUser={}，content={}", toUser, content);
            //随机休眠 3-5 秒，模拟发送短信.
            Thread.sleep((new Random().nextInt(2) + 3) * 1000);
            logger.info("发送短信结束：toUser={}，content={}", toUser, content);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
