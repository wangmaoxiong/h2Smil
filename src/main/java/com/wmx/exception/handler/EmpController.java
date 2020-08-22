package com.wmx.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工控制层。演示 @ExceptionHandler 注解统一处理控制层异常
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/8/20 19:32
 */
@RestController(value = "handlerEmpController")
public class EmpController {

    private static Logger logger = LoggerFactory.getLogger(EmpController.class);

    @Resource(name = "handlerEmpServiceImpl")
    private EmpServiceImpl empServiceImpl;

    /**
     * 根据员工id查询员工数据
     * http:localhost:8080/handler/emp/findEmpById?empId=2000
     *
     * @param empId
     * @return
     */
    @GetMapping(value = "handler/emp/findEmpById")
    public Map<String, Object> findEmpById(@RequestParam Integer empId) {
        Map<String, Object> resultData = new HashMap<>(4);
        System.out.println("进入员工控制层:【" + empId + "】");
        // 如果自己 try-catch 捕获了异常，没有继续往外抛时，则不会再走 @ExceptionHandler 方法捕获异常。
        Map<String, Object> emp = empServiceImpl.findEmpById(empId);
        resultData.put("code", 200);
        resultData.put("data", emp);
        return resultData;
    }

    /**
     * ExceptionHandler：异常处理注解，只有一个 value 属性，用于捕获特定的异常类型，为空时表示捕获所有异常
     *
     * @param ex ：目标方法实际抛出的异常对象
     * @return ：原 @RequestMapping 方法的返回值不会再走，而是会使用 @ExceptionHandler 方法的返回值，
     * 比如页面跳转，或者将数据返回给页面等等，而且两者的返回值类型可以不一致，比如原来是 Map,现在是 String。
     */
    @ExceptionHandler(value = {NumberFormatException.class})
    public Map<String, Object> numberFormatException(Exception ex) {
        System.out.println("控制层抛出异常=" + ex.getMessage());
        // 1、使用日志框架记录日志信息
        logger.error(ex.getMessage(), ex);

        // 2、实际生产中，后台通常会使用统一的数据对象返回给页面，比如状态码，错误消息，数据对象等，这里使用 Map 代替
        Map<String, Object> resultData = new HashMap<>(4);
        resultData.put("code", 500);
        resultData.put("msg", ex.getMessage());
        resultData.put("data", null);
        return resultData;
    }

    /**
     * ExceptionHandler：异常处理注解，只有一个 value 属性，用于捕获特定的异常类型，为空时表示捕获所有异常
     *
     * @param ex ：目标方法实际抛出的异常对象
     * @return ：原 @RequestMapping 方法的返回值不会再走，而是会使用 @ExceptionHandler 方法的返回值，
     * 比如页面跳转，或者将数据返回给页面等等，而且两者的返回值类型可以不一致，比如原来是 Map,现在是 String。
     */
    @ExceptionHandler()
    public Map<String, Object> exceptionHandler(Exception ex) {
        System.out.println("控制层抛出异常:" + ex.getMessage());
        // 1、使用日志框架记录日志信息
        logger.error(ex.getMessage(), ex);

        // 2、实际生产中，后台通常会使用统一的数据对象返回给页面，比如状态码，错误消息，数据对象等，这里使用 Map 代替
        Map<String, Object> resultData = new HashMap<>(4);
        resultData.put("code", 500);
        resultData.put("msg", "服务器忙！");
        resultData.put("data", null);
        return resultData;
    }

}
