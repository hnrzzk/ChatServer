package com.prefect.dbpool.utils;

import com.prefect.dbpool.DBBean;

import java.util.Properties;

/**
 * Created by zhangkai on 2017/1/9.
 */
public class Config {
    private String filePath = "dbpoolconfig.properties";

    public Config() {
        this("dbpoolconfig.properties");
    }

    public Config(String filePath) {
        this.filePath = filePath;
    }

    public DBBean getServerConf() throws Exception {
        String DRIVER_NAME = "driverClassName";
        String URL = "url";
        String USER_NAME = "username";
        String PASSWORD = "password";
        String MIN_CONNECTIONS = "initialSize";
        String MAX_CONNECTIONS = "maxActive";
        String POOL_NAME = "pollName";

        ClassLoader classLoader = Config.class.getClassLoader();
        if (classLoader != null) {
            System.out.println("项目路径：" + classLoader.getResource(filePath).toString());
        } else {
            System.out.println("项目路径：" + ClassLoader.getSystemResource(filePath).getPath());
        }

        Properties properties = new Properties();
        properties.load(Config.class.getClassLoader().getResourceAsStream(filePath));

        DBBean bean = new DBBean();
        bean.setDriverName(properties.getProperty(DRIVER_NAME));
        bean.setUrl(properties.getProperty(URL));
        bean.setUserName(properties.getProperty(USER_NAME));
        bean.setPassword(properties.getProperty(PASSWORD));

        bean.setMinConnections(Integer.parseInt(properties.getProperty(MIN_CONNECTIONS)));
        bean.setMaxConnections(Integer.parseInt(properties.getProperty(MAX_CONNECTIONS)));

        return bean;
    }
}
