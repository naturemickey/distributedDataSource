package org.w01f.dds.layer1.dsproxy.param;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;

public class Param {

    private Method method;
    private Object[] args;

    public Param(Method method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    public Param(String methodName, Object[] args) throws NoSuchMethodException {
        this(PreparedStatement.class.getDeclaredMethod(methodName, String.class), args);
    }

    public void putValue(PreparedStatement preparedStatement, int index) {
        args[0] = index;
        try {
            method.invoke(preparedStatement, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Object[] getValue() {
        return this.args;
    }
}

