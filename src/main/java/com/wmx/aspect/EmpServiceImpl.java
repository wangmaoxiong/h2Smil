package com.wmx.aspect;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 员工业务层
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/8/20 19:32
 */
@Service
public class EmpServiceImpl {

    /**
     * 根据员工id查询员工数据
     *
     * @param empId
     * @return
     */
    public Map<String, Object> findEmpById(Integer empId) {
        System.out.println("根据员工id查询员工数据【" + empId + "】");
        Map<String, Object> empMap = new HashMap<>(8);
        empMap.put("id", empId);
        empMap.put("name", "张无忌");
        empMap.put("age", 33);
        empMap.put("birthday", new Date());

        try {
            // 模拟方法执行耗时
            int nextInt = new Random().nextInt(3000);
            TimeUnit.MILLISECONDS.sleep(nextInt + 200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return empMap;
    }

    /**
     * 根据员工id删除员工数据
     *
     * @param empId
     */
    public void deleteById(Integer empId) {
        System.out.println(1 / empId);
        System.out.println("根据员工id删除员工数据【" + empId + "】");

        try {
            // 模拟方法执行耗时
            int nextInt = new Random().nextInt(3000);
            TimeUnit.MILLISECONDS.sleep(nextInt + 200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
