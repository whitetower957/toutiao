package com.nowcoder.aspect;




import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;


@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(Aspect.class);

    @Before("execution(* com.nowcoder.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
//        添加切点的参数
        for (Object arg:
             joinPoint.getArgs()) {
            sb.append("arg:" + arg.toString() + "|");
        }
//        打印切点参数信息
        logger.info("before method: " + sb.toString());
        logger.info("before time:" + new Date());
    }

    @After("execution(* com.nowcoder.controller.IndexController.*(..))")
    public void afterMethod(JoinPoint joinPoint){
//        打印切点参数信息
        logger.info("after method: ");
        logger.info("after time:" + new Date());
    }
}
