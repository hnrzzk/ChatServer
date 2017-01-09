package com.prefect.dbpool;

import java.sql.Connection;

/**
 * Created by hnrzz on 2017/1/8.
 */
public class DBConnectionSource {
    private IConnectionPool pool;

    public DBConnectionSource() {
        pool = ConnectionPoolManager.getInstance().getPool();
    }
    public Connection getConnection() {
        Connection conn = null;
        if (pool != null && pool.isActive()) {
            conn = pool.getConnection();
        }

        return conn;
    }
}
