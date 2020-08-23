package com.wmx.exception.advice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 部门控制层。演示 @ExceptionHandler 注解统一处理控制层异常
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/8/20 19:32
 */
@RestController
public class DeptController {

    @Resource
    private DeptServiceImpl deptService;

    /**
     * 根据员工id查询员工数据
     * http://localhost:8080/dept/findDeptById?deptId=2000
     *
     * @param deptId
     * @return
     */
    @GetMapping(value = "dept/findDeptById")
    public Map<String, Object> findEmpById(@RequestParam Integer deptId) {
        Map<String, Object> resultData = new HashMap<>(4);
        System.out.println("进入部门控制层:【" + deptId + "】");
        // 如果自己 try-catch 捕获了异常，没有继续往外抛时，则不会再走 @ExceptionHandler 方法捕获异常。
        Map<String, Object> emp = deptService.findEmpById(deptId);
        resultData.put("code", 200);
        resultData.put("data", emp);
        return resultData;
    }
}
