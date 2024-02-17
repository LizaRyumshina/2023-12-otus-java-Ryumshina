package ru.piggyBank.ioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.piggyBank.MoneyBoxInterface;
import ru.piggyBank.PiggyBank;

public class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private static final Map<Method, Boolean> methodCache = new HashMap<>();
    private Ioc() {}
    public static MoneyBoxInterface createPiggyBank() {
        System.out.println("createPiggyBank");
        InvocationHandler handler = new AddLogPiggyBankHandler(new PiggyBank());
        return (MoneyBoxInterface)
                Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[] {MoneyBoxInterface.class}, handler);
    }
    static class AddLogPiggyBankHandler implements InvocationHandler {
        private final MoneyBoxInterface piggyBank;

        public AddLogPiggyBankHandler(PiggyBank piggyBank) { this.piggyBank = piggyBank;}

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (!methodCache.containsKey(method)) {
                Method curMethod = piggyBank.getClass().getMethod(method.getName(), method.getParameterTypes());
                methodCache.put(method, curMethod.isAnnotationPresent(Log.class));
            }
            if (methodCache.get(method)) {
                logger.info("executed method:{}, param:{}", method.getName(), getParameters(method, args));
            }
            return method.invoke(piggyBank, args);
        }
        private String getParameters(Method method, Object[] args){
            if((args == null) || args.length==0){
                return "no parameters";
            }else if (args[0] instanceof int[]) {
                return Arrays.toString((int[])args[0]);
            } else {
                return args[0].toString();
            }
        }
    }
}
