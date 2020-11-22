package com.zjee.nio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectorTest {
    static Map map = new HashMap();

    public static void main(String[] args) {
        List a = new ArrayList();

        foo("a", a);
        System.out.println(map);
        a.add(1);
        foo("b", a);
        System.out.println(map);
    }

    public static void foo(String k, List a){
        map.put(k, a);
    }
}
