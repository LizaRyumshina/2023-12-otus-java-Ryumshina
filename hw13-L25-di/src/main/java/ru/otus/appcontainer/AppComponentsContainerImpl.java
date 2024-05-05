package ru.otus.appcontainer;

import java.util.*;
import java.lang.reflect.Method;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {
    private static final Logger log = LoggerFactory.getLogger(AppComponentsContainerImpl.class);
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {

        log.info("Start processConfig Classes");
        processConfig(initialConfigClasses);
    }

    public AppComponentsContainerImpl(String initialConfigClasses) {
        log.info("AppComponentsContainerImpl String");
        Reflections reflections = new Reflections(initialConfigClasses);
        processConfig(reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class).toArray(new Class[0]));
    }

    private void processConfig(Class<?>... configClasses) {
        log.info("Start processConfig: {} ", Arrays.toString(configClasses));

        List<Method> allConfigMethods = new ArrayList<>();
        for (Class<?> configClass : sortClassesByOrder(configClasses)) {
            checkConfigClass(configClass);

            List<Method> methods = getAnnotatedMethods(configClass);
            Object instanceConfigClass = getInstance(configClass);
            for (Method method : methods) {
                processMethod(method, instanceConfigClass);
            }
            allConfigMethods.addAll(methods);
        }
        sortMethodsByOrder(allConfigMethods);
    }

    private void processMethod(Method method, Object instanceConfigClass) {
        log.info("Start processMethod for {}", method.getName());
        String annotationName = method.getAnnotation(AppComponent.class).name();

        if (checkDuplicationMethod(annotationName)) {
            log.error("check duplication failed {}, {}", method.getName(), annotationName);
            throw new RuntimeException(String.format("check duplication failed %s, %s", method.getName(), annotationName));
        } else {
            Object component;
            if (method.getParameterCount() == 0) {
                component = invokeMethod(method, instanceConfigClass);
            } else {
                Object[] dependencies = getDependencies(method);
                component = invokeMethod(method, instanceConfigClass, dependencies);
            }
            appComponents.add(component);
            appComponentsByName.put(annotationName, component);
        }
    }

    private Object invokeMethod(Method method, Object instanceConfigClass) {
        try {
            return method.invoke(instanceConfigClass);
        } catch (Exception e) {
            log.error(" Error InvokeMethod {} ", method.getName());
            throw new RuntimeException(e);
        }
    }

    private Object invokeMethod(Method method, Object instanceConfigClass, Object[] dependencies) {
        try {
            return method.invoke(instanceConfigClass, dependencies);
        } catch (Exception e) {
            log.error(" Error invokeMethod with dependencies {} {} ", method.getName(), Arrays.stream(dependencies).toArray());
            throw new RuntimeException(e);
        }
    }

    private void sortMethodsByOrder(List<Method> allConfigMethods) {
        allConfigMethods.sort((Comparator.comparingInt(o -> o.getDeclaredAnnotation(AppComponent.class).order())));
    }

    private Class<?>[] sortClassesByOrder(Class<?>[] configClasses) {
        return Arrays.stream(configClasses).sorted(Comparator.comparingInt(c -> c.getDeclaredAnnotation(AppComponentsContainerConfig.class).order())).toArray(Class<?>[]::new);
    }

    private Object[] getDependencies(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = getAppComponent(parameterTypes[i]);
        }
        return parameters;
    }

    private boolean checkDuplicationMethod(String annotationName) {
        return appComponentsByName.containsKey(annotationName);
    }

    private Object getInstance(Class<?> configClass) {
        try {
            return configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("getInstances failed on {} ", configClass.getName());
            throw new RuntimeException(String.format("getInstances failed: %s", e.getMessage()));
        }
    }

    private List<Method> getAnnotatedMethods(Class<?> configClass) {
        List<Method> methods = new ArrayList<>();
        for (Method method : configClass.getDeclaredMethods()) {
            log.info("method name = {}", method.getName());
            if (method.isAnnotationPresent(AppComponent.class)) {
                log.info(method.getName() + " is annotated");
                methods.add(method);
            }
        }
        sortMethodsByOrder(methods);
        log.info(methods.toString());
        return methods;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        log.info("getAppComponent Class = {}", componentClass.getName());
        List<Object> components = appComponents.stream().filter(componentClass::isInstance).collect(Collectors.toList());
        if (components.size() != 1) {
            throw new RuntimeException("Duplicate found");
        }
        return componentClass.cast(components.getFirst());
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        log.info("getAppComponent string = " + componentName);
        Object object = appComponentsByName.get(componentName);
        if (Objects.isNull(object)) {
            log.error("getAppComponent is null for {}", componentName);
            throw new RuntimeException(String.format("Given string component is undefined: %s", componentName));
        }
        return (C) object;
    }

}
