package org.w01f.dds.layer5;

import java.sql.SQLException;

public class TransactionManager {

    private DataSourceProxy dataDataSourceProxy;
    private DataSourceProxy indexDataSourceProxy;

    public void setDataDataSourceProxy(DataSourceProxy dataDataSourceProxy) {
        this.dataDataSourceProxy = dataDataSourceProxy;
    }

    public void setIndexDataSourceProxy(DataSourceProxy indexDataSourceProxy) {
        this.indexDataSourceProxy = indexDataSourceProxy;
    }

    private ThreadLocal<Boolean> isTransactionStart = new ThreadLocal<>();

    public void beginTransaction() {
        isTransactionStart.set(true);
    }

    public void commit() throws SQLException {
        try {
            checkTransaction();
            indexDataSourceProxy.commitAll();
            dataDataSourceProxy.commitAll();
        } finally {
            isTransactionStart.set(false);
        }
    }

    public void rollback() throws SQLException {
        try {
            checkTransaction();
            indexDataSourceProxy.rollbackAll();
            dataDataSourceProxy.rollbackAll();
        } finally {
            isTransactionStart.set(false);
        }
    }

    private void checkTransaction() {
        Boolean isStarted = isTransactionStart.get();
        if (isStarted == null || isStarted == false)
            throw new RuntimeException("db transcation does not start.");
    }
}
