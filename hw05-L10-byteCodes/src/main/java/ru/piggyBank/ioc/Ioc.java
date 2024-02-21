package ru.piggyBank.ioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.piggyBank.MoneyBoxInterface;
import ru.piggyBank.PiggyBank;

public class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {}
    public static MoneyBoxInterface createPiggyBank() {
        System.out.println("createPiggyBank");
        InvocationHandler handler = new AddLogPiggyBankHandler(new PiggyBank());
        return (MoneyBoxInterface)
                Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[] {MoneyBoxInterface.class}, handler);
    }

    static class AddLogPiggyBankHandler implements InvocationHandler {
        private static HashSet<String> methodCache = new HashSet<String>();
        private final MoneyBoxInterface piggyBank;

        public AddLogPiggyBankHandler(PiggyBank piggyBank) {
            this.piggyBank = piggyBank;
            for(Method method:piggyBank.getClass().getDeclaredMethods()){
                if (method.isAnnotationPresent(Log.class)){
                    methodCache.add(getHashKey(method));
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methodCache.contains(getHashKey(method))) {
                logger.info("executed method:{}, param:{}", method.getName(), getParameters(method, args));
            }
            return method.invoke(piggyBank, args);
        }
        private String getParameters(Method method, Object[] args){
            if((args == null) || args.length==0){
                return "no parameters";
            }else {
                return Arrays.toString(args);
            }
        }
        private static String getHashKey(Method method) {
            //System.out.println(method.getName()+" " + Arrays.toString(method.getParameterTypes()));
            return method.getName()+ Arrays.toString(method.getParameterTypes());
        }
    }
}