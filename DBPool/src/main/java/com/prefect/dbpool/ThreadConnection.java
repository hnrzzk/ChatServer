package com.prefect.dbpool;

import java.sql.Connection;

/**
 * Created by hnrzz on 2017/1/8.
 */
public class ThreadConnection implements Runnable{
    private IConnectionPool pool;
    @Override
    public void run() {
        pool = ConnectionPoolManager.getInstance().getPool("testPool");
    }

    public Connection getConnection(){
        Connection conn = null;
        if(pool != null && pool.isActive()){
            conn = pool.getConnection();
        }
        return conn;
    }

    public Connection getCurrentConnection(){
        Connection conn = null;
        if(pool != null && pool.isActive()){
            conn = pool.getCurrentConnecton();
        }
        return conn;
    }
}
