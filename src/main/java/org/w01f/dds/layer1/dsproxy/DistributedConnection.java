package org.w01f.dds.layer1.dsproxy;

import org.w01f.dds.utils.GeneralCallPrinterBase;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class DistributedConnection extends GeneralCallPrinterBase {

    private Connection connection;

    public DistributedConnection(Connection connection) {
        super(Connection.class);
        this.connection = connection;
    }

    @Override
    protected Object getObj() {
        return connection;
    }
}
