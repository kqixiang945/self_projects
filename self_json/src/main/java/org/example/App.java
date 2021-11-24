package org.example;

import org.example.datax.Configuration;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Method[] accessibleMethods = getAccessibleMethods(Configuration.class);
        for (Method accessibleMethod : accessibleMethods) {
            System.out.println(accessibleMethod.getName());
        }
    }

    public static Method[] getAccessibleMethods(Class clazz) {
        List<Method> result = new ArrayList<Method>();
        while (clazz != null) {
            for (Method method : clazz.getDeclaredMethods()) {
                int modifiers = method.getModifiers();
                if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
                    result.add(method);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return result.toArray(new Method[result.size()]);
    }
}
