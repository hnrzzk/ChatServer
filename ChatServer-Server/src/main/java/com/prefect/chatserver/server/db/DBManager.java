package com.prefect.chatserver.server.db;


import com.prefect.dbpool.DBConnectionSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 通过dbcp管理数据库连接 单例
 * Created by zhangkai on 2016/12/26.
 */
public class DBManager {
    String filePath = "dbcpconfig.properties";

    private final static Logger logger = LoggerFactory.getLogger(DBManager.class);

    private static class DBUtilHandler {
        private static DBManager instance = new DBManager();
    }

    public static DBManager getInstance() {
        return DBUtilHandler.instance;
    }

        private DataSource ds = null;
//    private DBConnectionSource ds;

    private DBManager() {
        Properties properties = new Properties();

        try {
            properties.load(DBManager.class.getClassLoader().getResourceAsStream(filePath));
            ds = BasicDataSourceFactory.createDataSource(properties);
//            ds = new DBConnectionSource();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }


    }

    public Connection getConnection() throws SQLException {
        Connection connection = ds.getConnection();
        return connection;
    }

    /**
     * 关闭closeResultSet
     *
     * @param rs
     */
    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        rs = null;
    }

    /**
     * 关闭statement
     *
     * @param stm
     */
    public static void closeStatement(Statement stm) {
        try {
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        stm = null;
    }

    /**
     * 关闭connection
     *
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        conn = null;
    }
}
