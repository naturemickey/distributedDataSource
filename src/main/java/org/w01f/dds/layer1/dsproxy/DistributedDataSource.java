package org.w01f.dds.layer1.dsproxy;


import org.w01f.dds.utils.GeneralCallPrinterBase;

import javax.sql.DataSource;

public class DistributedDataSource extends GeneralCallPrinterBase {
    private DataSource ds;

    public void setDs(DataSource ds) {
        this.ds = ds;
    }

    public DistributedDataSource() {
        super(DataSource.class);
    }

    @Override
    protected Object getObj() {
        return ds;
    }
}
