package org.w01f.dds.layer1.dsproxy.param;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.PreparedStatement;

class ValueGetter implements InvocationHandler {

    private Object proxy;
    private Object[] value;

    ValueGetter() {
        proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[]{PreparedStatement.class}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        value = args;
        return null;
    }

    public PreparedStatement getProxy() {
        return (PreparedStatement) proxy;
    }

    public Object[] getValue() {
        return value;
    }
}