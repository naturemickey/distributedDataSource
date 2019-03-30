package org.w01f.dds.layer1.dsproxy;

import org.w01f.dds.layer2.sql.SqlHandler;
import org.w01f.dds.layer2.sql.parser.mysql.ParserUtils;
import org.w01f.dds.layer2.sql.parser.mysql.tree.ElementPlaceholderNode;
import org.w01f.dds.layer2.sql.parser.mysql.tree.StatNode;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.List;
import java.util.function.BiConsumer;

public class DistributedPreparedStatement extends DistributedStatement implements PreparedStatement {

    private PreparedStatement statement;
    private StatNode statNode;
    private SqlHandler sqlHandler = new SqlHandler();

    public DistributedPreparedStatement(PreparedStatement statement, String sql) {
        super(statement);
        this.statement = statement;
        this.statNode = ParserUtils.parse(sql);
    }

    private void prepareSetter() {
        List<ElementPlaceholderNode> placeholderNodes = this.statNode.getPlaceholderNodes();
        for (int i = 1; i <= placeholderNodes.size(); ++i) {
            BiConsumer<PreparedStatement, Integer> setter = this.params.getSetter(i);
            if (setter == null) {
                throw new RuntimeException("parameter index set error. you didn't set no " + i);
            }
            ElementPlaceholderNode placeholderNode = placeholderNodes.get(i - 1);
            placeholderNode.setSetter(setter);
        }
    }

    private void clearSetter() {
        List<ElementPlaceholderNode> placeholderNodes = this.statNode.getPlaceholderNodes();
        for (ElementPlaceholderNode placeholderNode : placeholderNodes) {
            placeholderNode.setSetter(null);
        }
    }

    private void setParams() {
        List<ElementPlaceholderNode> placeholderNodes = this.statNode.getPlaceholderNodes();
        for (int i = 0, len = placeholderNodes.size(); i < len; ++i) {
            ElementPlaceholderNode elementPlaceholderNode = placeholderNodes.get(i);
            elementPlaceholderNode.setter().accept(statement, i + 1);
        }
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        prepareSetter();
        setParams();
        sqlHandler.executeQuery(this.statNode);
        // TODO
        return statement.executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        prepareSetter();
        setParams();
        sqlHandler.executeUpdate(this.statNode);
        return statement.executeUpdate();
    }

    @Override
    public boolean execute() throws SQLException {
        prepareSetter();
        setParams();
        sqlHandler.execute(this.statNode);
        return statement.execute();
    }

    @Override
    public void clearParameters() throws SQLException {
        clearSetter();
        statement.clearParameters();
    }

    // These methods are not supported:

    @Override
    public void addBatch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResultSetMetaData getMetaData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParameterMetaData getParameterMetaData() {
        throw new UnsupportedOperationException();
    }

    // These set methods do not need to implement.

    @Override
    public void setNull(int parameterIndex, int sqlType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setByte(int parameterIndex, byte x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setShort(int parameterIndex, short x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setInt(int parameterIndex, int x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLong(int parameterIndex, long x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFloat(int parameterIndex, float x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDouble(int parameterIndex, double x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setString(int parameterIndex, String x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDate(int parameterIndex, Date x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTime(int parameterIndex, Time x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setObject(int parameterIndex, Object x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRef(int parameterIndex, Ref x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setClob(int parameterIndex, Clob x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setArray(int parameterIndex, Array x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setURL(int parameterIndex, URL x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNString(int parameterIndex, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) {
        throw new UnsupportedOperationException();
    }
}