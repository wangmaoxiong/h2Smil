package com.wmx.aspect;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    }

}
