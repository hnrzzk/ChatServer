package com.prefect.chatserver.commoms.util.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 数据库连接单元
 * Created by zhangkai on 2016/12/28.
 */
public class ChatServerDbConnectUnit {
    public ChatServerDbConnectUnit(ResultSet resultSet, Statement statement, Connection connection) {
        this.resultSet = resultSet;
        this.statement = statement;
        this.connection = connection;
    }

    private ResultSet resultSet;
    private Connection connection;
    private Statement statement;

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

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public void close() {
        DBManager.closeResultSet(this.resultSet);
        DBManager.closeStatement(this.statement);
        DBManager.closeConnection(this.connection);
    }
}
