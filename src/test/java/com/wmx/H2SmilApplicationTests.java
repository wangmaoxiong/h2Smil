package com.wmx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class H2SmilApplicationTests {

    /**
     * Spring Boot 引用了 JPA 组件，JPA 组件引用了 JDBC，JDBC组件引用了 HikariCP 数据源
     * Spring Boot 默认使用 class com.zaxxer.hikari.HikariDataSource 数据源，
     * 程序员直接 DI 注入然后使用即可
     */
    @Resource
    DataSource dataSource;

    @Test
    public void contextLoads() throws SQLException {
        Connection connection = dataSource.getConnection();//获取连接
        System.out.println("数据源>>>>>>" + dataSource.getClass());
        System.out.println("连接>>>>>>>>>" + connection);
        System.out.println("连接地址>>>>>" + connection.getMetaData().getURL());
        connection.close();//关闭连接
    }
}