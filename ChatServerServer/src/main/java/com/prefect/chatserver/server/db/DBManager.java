package com.prefect.chatserver.server.db;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
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
    private final static Logger logger = LoggerFactory.getLogger(DBManager.class);

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
            logger.error(e.getMessage(),e);
        }


    }

    public Connection getConnection() throws SQLException {
            return ds.getConnection();
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        }

        rs = null;
    }

    public static void closeStatement(Statement stm) {
        try {
            if (stm != null) {
                stm.close();
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        }

        stm = null;
    }

    public static void closeConnection(Connection conn){
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        }

        conn = null;
    }
}
