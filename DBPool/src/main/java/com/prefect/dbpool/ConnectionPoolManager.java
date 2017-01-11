package com.prefect.dbpool;

import com.prefect.dbpool.utils.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by hnrzz on 2017/1/8.
 */
public class ConnectionPoolManager {
    private static Logger logger = LoggerFactory.getLogger(ConnectionPoolManager.class);
    // 连接池存放
    public IConnectionPool pools;

    private ConnectionPoolManager() {
        init();
    }

    public static ConnectionPoolManager getInstance() {
        return ConnectionPoolManagerHandle.instance;
    }

    private static class ConnectionPoolManagerHandle {
        private static ConnectionPoolManager instance = new ConnectionPoolManager();
    }

    /**
     * 初始化连接池
     */
    public void init() {
        DBBean bean = getDBBean();
        ConnectionPool pool = new ConnectionPool(bean);
        if (pool != null) {
            pools = pool;
        }
    }

    /**
     * 关闭，回收连接
     *
     * @param
     * @param conn
     */
    public void close(Connection conn) {
        try {
            if (pools != null) {
                pools.releaseConn(conn);
            }
        } catch (SQLException e) {
            logger.error("连接池已经销毁," + e.getMessage(), e);
        }
    }

    // 获得连接池
    public IConnectionPool getPool() {
        return pools;
    }

    private DBBean getDBBean() {
        DBBean bean;
        try {
            bean = new Config().getServerConf();
        } catch (Exception e) {
            logger.warn("读取配置文件失败，启用默认设置!");
            bean = new DBBean();
            bean.setDriverName("com.mysql.jdbc.Driver");
            bean.setUrl("jdbc:mysql://localhost/chatserver");
            bean.setUserName("root");
            bean.setPassword("root");

            bean.setMinConnections(5);
            bean.setMaxConnections(100);

        }
        return bean;
    }
}
