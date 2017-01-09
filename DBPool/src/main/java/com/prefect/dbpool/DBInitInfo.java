package com.prefect.dbpool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hnrzz on 2017/1/8.
 */
public class DBInitInfo {
    public  static List<DBBean> beans = null;
    static{
        beans = new ArrayList<DBBean>();
        // 这里数据 可以从xml 等配置文件进行获取
        // 为了测试，这里我直接写死
        DBBean beanOracle = new DBBean();
        beanOracle.setDriverName("com.mysql.jdbc.Driver");
        beanOracle.setUrl("jdbc:mysql://localhost/chatserver");
        beanOracle.setUserName("root");
        beanOracle.setPassword("root");

        beanOracle.setMinConnections(5);
        beanOracle.setMaxConnections(100);

        beanOracle.setPoolName("testPool");
        beans.add(beanOracle);
    }
}