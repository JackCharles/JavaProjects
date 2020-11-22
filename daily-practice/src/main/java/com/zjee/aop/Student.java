package com.zjee.aop;

import com.zjee.spring.Dog;
import lombok.Data;

@Data
public class Student implements Flyable {
    private String name;
    private int age;

    @Override
    public String getLongName(String perfix, Dog dog) {
        return perfix + name + dog.getAge();
    }
}
