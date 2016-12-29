package com.prefect.chatserver.commoms.util.db;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 通过dbcp管理数据库连接
 * Created by zhangkai on 2016/12/26.
 */
public class DBManager {

    private static class DBUtilHandler {
        private static DBManager instance = new DBManager();
    }

    public static DBManager getInstance() {
        return DBUtilHandler.instance;
    }

    private DataSource ds = null;

    private DBManager() {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream("dbcpconfig.properties"));
            ds= BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("获取数据库连接失败！error:" + e);
        }
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        rs = null;
    }

    public static void closeStatement(Statement stm) {
        try {
            if (stm != null) {
                stm.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        stm = null;
    }

    public static void closeConnection(Connection conn){
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        conn = null;
    }
}
