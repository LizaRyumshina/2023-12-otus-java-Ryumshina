package TestFramework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ProcessingAnnotation {
    public static void Run(String className, Statistic statistic) {
        try {
            Class<?> clazz = Class.forName(className);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            Method[] methods = clazz.getDeclaredMethods();

            List<Method> methodsBefore = GetMethods(clazz, methods, Before.class);
            List<Method> methodsAfter  = GetMethods(clazz, methods, After.class);

            for(Method method: GetMethods(clazz, methods, Test.class)){
                try {
                    invokeMethods(instance, methodsBefore);
                    System.out.println("run method: "+className+"."+method.getName());
                    method.invoke(instance);
                    statistic.Passed();
                } catch (Exception e) {
                    System.out.println("Test failed: " + method.getName()+ ". Error: "+ e.getCause());
                    statistic.Failed();
                } finally {
                    invokeMethods(instance, methodsAfter);
                }
            }
        } catch ( Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }

    private static List<Method> GetMethods(Class<?> clazz, Method[] methods, Class<? extends Annotation> annotation) {
        List<Method> result = new ArrayList<>();
        for(int i =0; i<methods.length;i++){
            if(methods[i]!= null && methods[i].isAnnotationPresent(annotation)){
                result.add(methods[i]);
            }
        }
        return result;
    }
    private static void invokeMethods(Object instance, List<Method> methods) throws Exception {
        for (Method method : methods) {
            method.invoke(instance);
        }
    }

}
