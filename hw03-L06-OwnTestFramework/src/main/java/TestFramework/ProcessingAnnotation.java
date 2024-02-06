package TestFramework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ProcessingAnnotation {
    public static void run(String className, Statistic statistic) {
        try {
            Class<?> clazzGeneral = Class.forName(className);
            Method[] methods = clazzGeneral.getDeclaredMethods();

            List<Method> methodsBefore = getMethods(clazzGeneral, methods, Before.class);
            List<Method> methodsAfter  = getMethods(clazzGeneral, methods, After.class);

            for(Method method: getMethods(clazzGeneral, methods, Test.class)){
                Class<?> clazzTest = Class.forName(className);
                Object instanceTest = clazzTest.getDeclaredConstructor().newInstance();
                try {
                    invokeMethods(instanceTest, methodsBefore);
                    System.out.println("run method: "+className+"."+method.getName());
                    method.invoke(instanceTest);
                    statistic.passed();
                } catch (Exception e) {
                    System.out.println("Test failed: " + method.getName()+ ". Error: "+ e.getCause());
                    statistic.failed();
                } finally {
                    invokeMethods(instanceTest, methodsAfter);
                }
            }
        } catch ( Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }

    private static List<Method> getMethods(Class<?> clazz, Method[] methods, Class<? extends Annotation> annotation) {
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
