package com.prefect.chatserver.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 数据库连接单元
 * Created by zhangkai on 2016/12/28.
 */
public class ChatServerDbConnectUnit {
    public ChatServerDbConnectUnit(ResultSet resultSet, PreparedStatement statement, Connection connection) {
        this.resultSet = resultSet;
        this.statement = statement;
        this.connection = connection;
    }

    private ResultSet resultSet;
    private Connection connection;
    private PreparedStatement statement;

    private int successRow;

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public PreparedStatement getStatement() {
        return statement;
    }

    public void setStatement(PreparedStatement statement) {
        this.statement = statement;
    }

    public int getSuccessRow() {
        return successRow;
    }

    public void setSuccessRow(int successRow) {
        this.successRow = successRow;
    }

    public void close() {
        DBManager.closeResultSet(this.resultSet);
        DBManager.closeStatement(this.statement);
        DBManager.closeConnection(this.connection);
    }

    protected void finalize(){
        this.close();
    }
}
