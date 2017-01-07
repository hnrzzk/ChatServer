package com.prefect.dbpool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by hnrzz on 2017/1/8.
 */
public interface IConnectionPool {
    // 获得连接
    public Connection getConnection();
    // 获得当前连接
    public Connection getCurrentConnecton();
    // 回收连接
    public void releaseConn(Connection conn) throws SQLException;
    // 销毁清空
    public void destroy();
    // 连接池是活动状态
    public boolean isActive();
    // 定时器，检查连接池
    public void cheackPool();
}