package com.prefect.dbpool;

import com.prefect.dbpool.utils.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hnrzz on 2017/1/8.
 */
public class DBInitInfo {
    public  static List<DBBean> beans = null;
    static{
        beans = new ArrayList<DBBean>();

        DBBean bean;
        try {
            bean= new Config().getServerConf();
        } catch (Exception e) {
            bean = new DBBean();
            bean.setDriverName("com.mysql.jdbc.Driver");
            bean.setUrl("jdbc:mysql://localhost/chatserver");
            bean.setUserName("root");
            bean.setPassword("root");

            bean.setMinConnections(5);
            bean.setMaxConnections(100);

            bean.setPoolName("testPool");
        }

        beans.add(bean);
    }
}