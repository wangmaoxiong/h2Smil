package com.wmx.excel.web;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/7/2 10:25
 */
@Service
public class ExcelService {
    public void save(List<ExcelWebData> list) {
        try {
            //模拟操作数据库耗时
            // 如果是mybatis,尽量别直接调用多次insert,自己写一个mapper里面新增一个方法batchInsert,所有数据一次性插入
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
