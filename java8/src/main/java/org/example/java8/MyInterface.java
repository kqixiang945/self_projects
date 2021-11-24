package org.example.java8;

public interface MyInterface {

    public static void show() {
        System.out.println("接口中的静态方法");
    }

    default String getName() {
        return "呵呵呵";
    }

}
