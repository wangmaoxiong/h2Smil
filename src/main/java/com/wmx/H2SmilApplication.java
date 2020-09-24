package com.wmx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author wangMaoXiong
 * EnableCaching ：开启缓存支持
 */
@SpringBootApplication
@EnableCaching
public class H2SmilApplication {

    public static void main(String[] args) {
        SpringApplication.run(H2SmilApplication.class, args);
    }

}
