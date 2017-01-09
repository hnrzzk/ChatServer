package com.prefect.dbpool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by hnrzz on 2017/1/8.
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {
        // 初始化连接池
        Thread t = init();
        t.start();
        t.join();

        DBConnectionSource a = new DBConnectionSource();
        DBConnectionSource b = new DBConnectionSource();
        DBConnectionSource c = new DBConnectionSource();


        System.out.println("线程A-> " + a.getConnection());
        System.out.println("线程B-> " + b.getConnection());
        System.out.println("线程C-> " + c.getConnection());

        Connection connection1 = a.getConnection();
        Connection connection2 = a.getConnection();

        try {
            PreparedStatement preparedStatement =
                    connection1.prepareStatement("select count(*) from user where is_online=0 ");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                System.out.println(resultSet.getInt(1));
            }

            resultSet.close();
            preparedStatement.close();
            connection1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        if (connection1 == connection2) {
//            System.out.println("twice connection is same!");
//        } else {
//            System.out.println("twice connection is not same!");
//        }
    }

    // 初始化
    public static Thread init() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                IConnectionPool pool = initPool();
                while (pool == null || !pool.isActive()) {
                    pool = initPool();
                }
            }
        });
        return t;
    }

    public static IConnectionPool initPool() {
        return ConnectionPoolManager.getInstance().getPool();
    }

}
