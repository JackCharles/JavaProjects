package com.zjee.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * 基于注解
 */

@Aspect
public class AspectTest {

    @Before("execution(void com.zjee.aop.Flyable.fly())")
    public void beforeFly(){
        System.out.println("before fly...");
    }
}
