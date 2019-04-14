package org.w01f.dds.layer1.dsproxy.param;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;

public class Params {

    private Map<Integer, Param> map = new TreeMap<>();

//    public void addParam(int parameterIntex, BiConsumer<PreparedStatement, Integer> setter) {
//        map.put(parameterIntex, new Param(setter));
//    }

    public void addParam(int parameterIndex, Method method, Object[] args) {
        map.put(parameterIndex, new Param(method, args));
    }

    public Param getParam(int parameterIndex) {
        return map.get(parameterIndex);
    }
}
