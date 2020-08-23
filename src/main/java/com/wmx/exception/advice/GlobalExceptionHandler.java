package com.wmx.exception.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理。
 * 1、@ControllerAdvice 表示为应用下所有的控制器给出建议，类中提供 @ExceptionHandler 异常处理方法，
 * 所以组合起来就是为整个应用下的所有控制器给出全局异常处理建议。
 * 2、如同 @RestController 注解组合了 @Controller 与 @ResponseBody 注解一样，@RestControllerAdvice
 * 也组合了 @ControllerAdvice 与 @ResponseBody 注解
 * 3、所以 @ControllerAdvice 中的  @ExceptionHandler 方法返回默认是做页面跳转，加上 @ResponseBody 才会直接返回给页面
 * 而 @RestControllerAdvice 中的 @ExceptionHandler 方法则默认是返回给页面。
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/8/22 17:27
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * ExceptionHandler：异常处理注解，只有一个 value 属性，用于捕获特定的异常类型，为空时表示捕获所有异常
     * ExceptionHandler 异常处理方法的返回值是做页面跳转还是直接返回给页面，取决于所在的类使用的是 ControllerAdvice 注解还是 RestControllerAdvice 注解，
     * ControllerAdvice 时 ExceptionHandler 方法的返回值默认是页面跳转（除非 ExceptionHandler 方法再加上 @ResponseBody 注解，此时就会直接返回给页面）；
     * RestControllerAdvice 时 @ExceptionHandler 方法返回值默认是直接返回给页面。
     *
     * @param ex ：目标方法实际抛出的异常对象
     * @return ：原 @RequestMapping 方法的返回值不会再走，而是会使用 @ExceptionHandler 方法的返回值，
     * 比如页面跳转，或者将数据返回给页面等等，而且两者的返回值类型可以不一致，比如原来是 Map,现在是 String。
     */
    @ExceptionHandler(value = {NumberFormatException.class})
    @ResponseBody
    public Map<String, Object> numberFormatException(Exception ex) {
        System.out.println("控制层异常=" + ex.getMessage());
        // 1、使用日志框架记录日志信息
        logger.error(ex.getMessage(), ex);

        // 2、实际生产中，后台通常会使用统一的数据对象返回给页面，比如状态码，错误消息，数据对象等，这里使用 Map 代替
        Map<String, Object> resultData = new HashMap<>(4);
        resultData.put("code", 500);
        resultData.put("data", null);
        resultData.put("msg", ex.getMessage());
        return resultData;
    }

    /**
     * ExceptionHandler：异常处理注解，只有一个 value 属性，用于捕获特定的异常类型，为空时表示捕获所有异常
     * ExceptionHandler 异常处理方法的返回值是做页面跳转还是直接返回给页面，取决于所在的类使用的是 ControllerAdvice 注解还是 RestControllerAdvice 注解，
     * ControllerAdvice 时 ExceptionHandler 方法的返回值默认是页面跳转（除非 ExceptionHandler 方法再加上 @ResponseBody 注解，此时就会直接返回给页面）；
     * RestControllerAdvice 时 @ExceptionHandler 方法返回值默认是直接返回给页面。
     *
     * @param ex ：目标方法实际抛出的异常对象
     * @return ：原 @RequestMapping 方法的返回值不会再走，而是会使用 @ExceptionHandler 方法的返回值，
     * 比如页面跳转，或者将数据返回给页面等等，而且两者的返回值类型可以不一致，比如原来是 Map,现在是 String。
     */
    @ExceptionHandler()
    @ResponseBody
    public Map<String, Object> exceptionHandler(Exception ex) {
        System.out.println("控制层异常:" + ex.getMessage());
        // 1、使用日志框架记录日志信息
        logger.error(ex.getMessage(), ex);

        // 2、实际生产中，后台通常会使用统一的数据对象返回给页面，比如状态码，错误消息，数据对象等，这里使用 Map 代替
        Map<String, Object> resultData = new HashMap<>(4);
        resultData.put("msg", "服务器忙！");
        resultData.put("code", 500);
        resultData.put("data", null);
        return resultData;
    }

}
