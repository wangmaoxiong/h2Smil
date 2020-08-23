package com.wmx.exception.advice;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 部门业务层
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/8/20 19:32
 */
@Service
public class DeptServiceImpl {

    private static final Map<String, Object> empMap = new HashMap<>(8);

    static {
        empMap.put("name", "云产品开发部");
        empMap.put("local", "湖南省长沙市雨花区");
        empMap.put("createTile", new Date());
    }

    /**
     * 根据部门id查询员工数据
     *
     * @param deptId
     * @return
     */
    public Map<String, Object> findEmpById(Integer deptId) {
        try {
            System.out.println("根据部门id查询员工数据【" + deptId + "】");
            empMap.put("deptId", deptId);
            // 模拟方法执行耗时（毫秒）
            int nextInt = new Random().nextInt(2000);
            TimeUnit.MILLISECONDS.sleep(nextInt);

            if (deptId == 1) {
                // 会抛出 ArrayIndexOutOfBoundsException 异常
                System.out.println(new String[]{"1", "2"}[10]);
            }
            if (deptId == 2) {
                // 会抛出 NullPointerException
                Long l = null;
                System.out.println(l.toString());
            }
            if (deptId == 3) {
                // 抛出 NumberFormatException
                throw new NumberFormatException("数值格式化异常！");
            }
        } catch (InterruptedException e) {
            // sleep 方法抛出的 InterruptedException 是编译异常，所以必须捕获处理获取继续往外抛
            // 假如业务层中捕获处理了某个 XxxException，则控制层就不会再触发捕获捕获，除非业务层继续往外抛。
            throw new RuntimeException("线程异常中断");
        }
        return empMap;
    }
}
