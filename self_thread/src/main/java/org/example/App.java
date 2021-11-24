package org.example;

import java.util.HashSet;
import java.util.Set;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Set<String> dbTableSet = new HashSet<>();
        dbTableSet.add("1");
        dbTableSet.add("2");
        dbTableSet.add("3");
        dbTableSet.add("4");
        dbTableSet.add("5");
        dbTableSet.add("6");
        dbTableSet.add("7");
        java8SetThread(dbTableSet);
        System.out.println("Hello World!");
    }


    /**
     * 对指定dbTableSet中的表 执行ANALYZE命令,做数据准备
     *
     * @param dbTableSet
     */
    private static void java8SetThread(Set<String> dbTableSet) {
        try {
            dbTableSet.parallelStream().forEach(dbTable -> System.out.println(dbTable));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
