package com.prefect.dbpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 * Created by hnrzz on 2017/1/8.
 */
public class ConnectionPoolManager {
    private static Logger logger = LoggerFactory.getLogger(ConnectionPoolManager.class);
    // 连接池存放
    public Hashtable<String, IConnectionPool> pools = new Hashtable<String, IConnectionPool>();

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
     * 初始化所有连接池
     */
    public void init() {
        for (int i = 0; i < DBInitInfo.beans.size(); i++) {
            DBBean bean = DBInitInfo.beans.get(i);
            ConnectionPool pool = new ConnectionPool(bean);
            if (pool != null) {
                pools.put(bean.getPoolName(), pool);
                logger.info("Init connection successed ->" + bean.getPoolName());
            }
        }
    }

    /**
     * 获得连接,根据连接池名字 获得连接
     * @param poolName
     * @return
     */
    public Connection getConnection(String poolName) {
        Connection conn = null;
        if (pools.size() > 0 && pools.containsKey(poolName)) {
            conn = getPool(poolName).getConnection();
        } else {
            logger.error("Can't find this connecion pool ->" + poolName);
        }
        return conn;
    }

    /**
     * 关闭，回收连接
     * @param poolName
     * @param conn
     */
    public void close(String poolName, Connection conn) {
        IConnectionPool pool = getPool(poolName);
        try {
            if (pool != null) {
                pool.releaseConn(conn);
            }
        } catch (SQLException e) {
            System.out.println("连接池已经销毁");
            e.printStackTrace();
        }
    }

    /**
     * 空连接池
     * @param poolName
     */
    public void destroy(String poolName) {
        IConnectionPool pool = getPool(poolName);
        if (pool != null) {
            pool.destroy();
        }
    }

    // 获得连接池
    public IConnectionPool getPool(String poolName) {
        IConnectionPool pool = null;
        if (pools.size() > 0) {
            pool = pools.get(poolName);
        }
        return pool;
    }
}
