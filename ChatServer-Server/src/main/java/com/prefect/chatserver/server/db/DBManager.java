package com.prefect.chatserver.server.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * 通过dbcp管理数据库连接 单例
 * Created by zhangkai on 2016/12/26.
 */
public class DBManager {

    SessionFactory sessionFactory;
    private static DBManager instance = new DBManager();

    private static class DBUtilHandler {
        private static DBManager instance = new DBManager();
    }

    public static DBManager getInstance() {
        return instance;
    }

    private DBManager() {
        Configuration configuration = new Configuration().configure();

        sessionFactory = configuration.buildSessionFactory();
    }

    /**
     * 从hibernae得到session
     * @return session
     */
    public Session getSession() {
        return sessionFactory.openSession();
    }

    protected void finalize(){
        if (sessionFactory.isOpen()){
            this.sessionFactory.close();
        }
    }
}
