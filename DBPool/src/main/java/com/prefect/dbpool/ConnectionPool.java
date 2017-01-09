package com.prefect.dbpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Created by hnrzz on 2017/1/8.
 */
public class ConnectionPool implements IConnectionPool {
    private static Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    // 连接池配置属性
    private DBBean dbBean;
    private boolean isActive = false; // 连接池活动状态
    private int contActive = 0;// 记录创建的总的连接数

    // 空闲连接
    private List<Connection> freeConnection = new Vector<Connection>();
    // 活动连接
    private List<Connection> activeConnection = new Vector<Connection>();

    public ConnectionPool(DBBean dbBean) {
        this.dbBean = dbBean;
        init();
        cheackPool();
    }

    /**
     * 初始化
     */
    public void init() {
        try {
            Class.forName(dbBean.getDriverName());
            for (int i = 0; i < dbBean.getInitConnections(); i++) {
                Connection conn;
                conn = newConnection();
                // 初始化最小连接数
                if (conn != null) {
                    freeConnection.add(conn);
                    contActive++;
                }
            }
            isActive = true;
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 获得连接
     *
     * @return
     */
    public synchronized Connection getConnection() {
        Connection conn = null;
        while (conn == null) {
            try {
                // 判断是否超过最大连接数限制 && 没有空闲连接
                while (contActive > this.dbBean.getMaxActiveConnections() && freeConnection.size() <= 0) {
                    this.wait();
                }
                this.notifyAll();

                if (freeConnection.size() > 0) {
                    //从空闲队列获取连接
                    conn = freeConnection.get(0);
                    freeConnection.remove(0);
                } else {
                    //新建连接
                    conn = newConnection();
                    contActive++;
                }

                //如果获取到的连接不可用
                if (isValid(conn)) {
                    activeConnection.add(conn);
                } else {
                    conn = null;
                    contActive--;
                }

            } catch (SQLException | ClassNotFoundException | InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return conn;
    }

    /**
     * 获得新连接
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private synchronized Connection newConnection()
            throws ClassNotFoundException, SQLException {
        Connection conn = null;
        if (dbBean != null) {
            Class.forName(dbBean.getDriverName());
            conn = new ChatServerDbConnection(DriverManager.getConnection(dbBean.getUrl(),
                    dbBean.getUserName(), dbBean.getPassword()));
        }
        return conn;
    }

    /**
     * 释放连接
     *
     * @param conn
     * @throws SQLException
     */
    public synchronized void releaseConn(Connection conn) throws SQLException {
        if (isValid(conn)) {
            freeConnection.add(conn);
        } else {
            contActive--;
        }
        activeConnection.remove(conn);
        // 唤醒所有正待等待的线程，去抢连接
        notifyAll();
    }

    /**
     * 判断连接是否可用
     *
     * @param conn
     * @return
     */
    private boolean isValid(java.sql.Connection conn) {
        try {
            if (conn == null || conn.isClosed()) {
                return false;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return true;
    }

    /**
     * 销毁连接池
     */
    public synchronized void destroy() {
        for (Connection conn : freeConnection) {
            try {
                if (isValid(conn)) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        for (Connection conn : activeConnection) {
            try {
                if (isValid(conn)) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        isActive = false;
    }

    // 连接池状态
    @Override
    public boolean isActive() {
        return isActive;
    }

    // 定时检查连接池情况
    @Override
    public void cheackPool() {
        logger.info("空线池连接数：" + freeConnection.size());
        logger.info("活动连接数：：" + activeConnection.size());
        logger.info("总的连接数：" + contActive);
        logger.info("允许的最大连接数：" + dbBean.getMaxActiveConnections());
    }
}
