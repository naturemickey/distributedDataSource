package org.w01f.dds.layer1.dsproxy.param;

import java.sql.PreparedStatement;
import java.util.function.BiConsumer;

public class Param {

    private BiConsumer<PreparedStatement, Integer> valueSetter;

    public Param(BiConsumer<PreparedStatement, Integer> setter) {
        this.valueSetter = setter;
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

