package com.zjee.spring;

import com.zjee.aop.Flyable;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpelTest {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:beans.xml");

        Flyable student = (Flyable) ctx.getBean("student");
        student.getLongName("long-", (Dog) ctx.getBean("dog"));
        System.out.println(ctx.getBean("dog"));
    }
}