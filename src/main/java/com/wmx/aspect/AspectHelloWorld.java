package com.wmx.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.SourceLocation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 切面注解 Aspect 使用入门
 * 1、@Aspect：声明本类为切面类
 * 2、@Component：将本类交由 Spring 容器管理
 * 3、@Order：指定切入执行顺序，数值越小，切面执行顺序越靠前，默认为 Integer.MAX_VALUE
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/8/20 19:22
 */
@Aspect
@Order(value = 999)
@Component
public class AspectHelloWorld {

    /**
     * @Pointcut ：切入点声明，即切入到哪些目标方法。value 属性指定切入点表达式，默认为 ""。
     * 用于被下面的通知注解引用，这样通知注解只需要关联此切入点声明即可，无需再重复写切入点表达式
     * <p>
     * 切入点表达式常用格式举例如下：
     * - * com.wmx.aspect.EmpService.*(..))：表示 com.wmx.aspect.EmpService 类中的任意方法
     * - * com.wmx.aspect.*.*(..))：表示 com.wmx.aspect 包(不含子包)下任意类中的任意方法
     * - * com.wmx.aspect..*.*(..))：表示 com.wmx.aspect 包及其子包下任意类中的任意方法
     * </p>
     */
    @Pointcut(value = "execution(* com.wmx.aspect.EmpServiceImpl.*(..))")
    private void aspectPointcut() {

    }

    /**
     * 前置通知：目标方法执行之前执行以下方法体的内容。
     * 如果在其中抛出异常，则目标方法不会再执行，会执行后置通知 -> 异常通知。
     * value：绑定通知的切入点表达式。可以关联切入点声明，也可以直接设置切入点表达式
     * <br/>
     *
     * @param joinPoint：提供对连接点处可用状态和有关它的静态信息的反射访问<br/> <p>
     *                                                 Object[] getArgs()：返回此连接点处（目标方法）的参数
     *                                                 Signature getSignature()：返回连接点处的签名。
     *                                                 Object getTarget()：返回目标对象
     *                                                 Object getThis()：返回当前正在执行的对象
     *                                                 StaticPart getStaticPart()：返回一个封装此连接点的静态部分的对象。
     *                                                 SourceLocation getSourceLocation()：返回与连接点对应的源位置
     *                                                 String toLongString()：返回连接点的扩展字符串表示形式。
     *                                                 String toShortString()：返回连接点的缩写字符串表示形式。
     *                                                 String getKind()：返回表示连接点类型的字符串
     *                                                 </p>
     */
    @Before(value = "aspectPointcut()")
    public void aspectBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        Object target = joinPoint.getTarget();
        Object aThis = joinPoint.getThis();
        JoinPoint.StaticPart staticPart = joinPoint.getStaticPart();
        SourceLocation sourceLocation = joinPoint.getSourceLocation();
        String longString = joinPoint.toLongString();
        String shortString = joinPoint.toShortString();

        System.out.println("【前置通知】");
        System.out.println("\targs=" + Arrays.asList(args));
        System.out.println("\tsignature=" + signature);
        System.out.println("\ttarget=" + target);
        System.out.println("\taThis=" + aThis);
        System.out.println("\tstaticPart=" + staticPart);
        System.out.println("\tsourceLocation=" + sourceLocation);
        System.out.println("\tlongString=" + longString);
        System.out.println("\tshortString=" + shortString);

        // 模拟参数校验未通过，抛出异常，此时目标方法不会再执行
        if(args !=null && args[0].equals(100)){
            throw new RuntimeException("参数校验失败！");
        }
    }

    /**
     * 后置通知：目标方法执行之后执行以下方法体的内容，不管目标方法是否发生异常。
     * value：绑定通知的切入点表达式。可以关联切入点声明，也可以直接设置切入点表达式
     */
    @After(value = "aspectPointcut()")
    public void aspectAfter(JoinPoint joinPoint) {
        System.out.println("【后置通知】");
        System.out.println("\tkind=" + joinPoint.getKind());
    }

    /**
     * 返回通知：目标方法返回后执行以下代码
     * value 属性：绑定通知的切入点表达式。可以关联切入点声明，也可以直接设置切入点表达式
     * pointcut 属性：绑定通知的切入点表达式，优先级高于 value，默认为 ""
     * returning 属性：通知签名中要将返回值绑定到的参数的名称，默认为 ""
     *
     * @param joinPoint ：提供对连接点处可用状态和有关它的静态信息的反射访问
     * @param result    ：目标方法返回的值，参数名称与 returning 属性值一致。无返回值时，这里 result 会为 null.
     */
    @AfterReturning(pointcut = "aspectPointcut()", returning = "result")
    public void aspectAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("【返回通知】");
        System.out.println("\t目标方法返回值=" + result);
    }

    /**
     * 异常通知：目标方法发生异常的时候执行以下代码，此时返回通知不会再触发
     * value 属性：绑定通知的切入点表达式。可以关联切入点声明，也可以直接设置切入点表达式
     * pointcut 属性：绑定通知的切入点表达式，优先级高于 value，默认为 ""
     * throwing 属性：与方法中的异常参数名称一致，
     *
     * @param ex：捕获的异常对象，名称与 throwing 属性值一致
     */
    @AfterThrowing(pointcut = "aspectPointcut()", throwing = "ex")
    public void aspectAfterThrowing(JoinPoint jp, Exception ex) {
        String methodName = jp.getSignature().getName();
        System.out.println("【异常通知】");
        if (ex instanceof ArithmeticException) {
            System.out.println("\t【" + methodName + "】方法算术异常（ArithmeticException）：" + ex.getMessage());
        } else {
            System.out.println("\t【" + methodName + "】方法异常：" + ex.getMessage());
        }
    }

}
