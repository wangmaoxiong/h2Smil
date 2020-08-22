package com.wmx.aspect;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工控制层
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/8/20 19:32
 */
@RestController
public class EmpController {

    @Resource
    private EmpServiceImpl empServiceImpl;

    /**
     * 根据员工id查询员工数据
     * http:localhost:8080/emp/findEmpById?empId=2000
     *
     * @param empId
     * @return
     */
    @GetMapping(value = "emp/findEmpById")
    public Map<String, Object> findEmpById(@RequestParam Integer empId) {
        Map<String, Object> resultData = new HashMap<>();
        try {
            System.out.println("进入员工控制层【" + empId + "】");
            Map<String, Object> emp = empServiceImpl.findEmpById(empId);
            resultData.put("code", 200);
            resultData.put("data", emp);
        } catch (Exception e) {
            resultData.put("code", 500);
            resultData.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return resultData;
    }

    /**
     * 根据员工id删除员工数据
     * http:localhost:8080/emp/deleteById?empId=2000
     *
     * @param empId
     * @return
     */
    @GetMapping(value = "emp/deleteById")
    public Map<String, Object> deleteById(@RequestParam Integer empId) {
        Map<String, Object> resultData = new HashMap<>();
        try {
            System.out.println("进入员工控制层【" + empId + "】");
            empServiceImpl.deleteById(empId);
            resultData.put("code", 200);
            resultData.put("msg", "success");
        } catch (Exception e) {
            resultData.put("code", 500);
            resultData.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return resultData;
    }

}
