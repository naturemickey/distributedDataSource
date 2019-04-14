package org.w01f.dds.layer1.dsproxy.param;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.function.BiConsumer;

public class Param {

    private BiConsumer<PreparedStatement, Integer> valueSetter;

//    public Param(BiConsumer<PreparedStatement, Integer> setter) {
//        this.valueSetter = setter;
//    }

    public Param(Method method, Object[] args) {
        this.valueSetter = (p, i) -> {
            Object[] params = new Object[args.length];
            params[0] = i;
            for (int x = 1; x < args.length; ++x) {
                params[x] = args[x];
            }
            try {
                method.invoke(p, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public Param(String methodName, Object[] args) throws NoSuchMethodException {
        this(PreparedStatement.class.getDeclaredMethod(methodName, String.class), args);
    }

    public void putValue(PreparedStatement preparedStatement, int index) {
        valueSetter.accept(preparedStatement, index);
    }

    public Object[] getValue() {
        ValueGetter getter = new ValueGetter();
        valueSetter.accept(getter.getProxy(), 1);
        return getter.getValue();
    }
}

