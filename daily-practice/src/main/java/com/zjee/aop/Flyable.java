package com.zjee.aop;

import com.zjee.spring.Dog;
import com.zjee.spring.Log;

public interface Flyable {
    default public void fly() {
        System.out.println("~~~~ fly ~~~~");
    }

    @Log(target = "#args[1].name")
    public String getLongName(String perfix, Dog dog);
}
