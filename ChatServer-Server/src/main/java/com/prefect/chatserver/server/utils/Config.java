package com.prefect.chatserver.server.utils;

import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;

/**
 * 读取配置类
 * Created by zhangkai on 2016/12/27.
 */
public class Config {
    private String systemConfigFile = "ChatServerConfig.properties";
    private String log4jConfigFile="log4j.properties";

    public Config() {
        this("ChatServerConfig.properties");
    }

    public Config(String filePath) {
        this.systemConfigFile = filePath;
    }

    public ServerInfo getServerConf() throws Exception {
        String SERVER_PORT = "server.port";
        String SERVER_TIME_OUT = "server.timeOut";
        String SERVER_IDLE_TIME = "server.IdleTime";
        String SERVER_BUFFER_SIZE = "server.bufferSize";
        String SERVER_HOSTNAME = "server.hostname";
        String SERVER_CACHE_SIZE = "server.cacheSize";

//        ClassLoader classLoader = Config.class.getClassLoader();
//        if (classLoader != null) {
//            System.out.println("项目路径：" + classLoader.getResource(systemConfigFile).toString());
//        } else {
//            System.out.println("项目路径：" + ClassLoader.getSystemResource(systemConfigFile).getPath());
//        }

        PropertyConfigurator.configure(Config.class.getClassLoader().getResourceAsStream(log4jConfigFile));

        Properties properties = new Properties();
        properties.load(Config.class.getClassLoader().getResourceAsStream(systemConfigFile));

        ServerInfo serverInfo = new ServerInfo();

        serverInfo.setHostname(properties.getProperty(SERVER_HOSTNAME));
        serverInfo.setPort(Integer.parseInt(properties.getProperty(SERVER_PORT)));
        serverInfo.setTimeOut(Integer.parseInt(properties.getProperty(SERVER_TIME_OUT)));
        serverInfo.setIdleTime(Integer.parseInt(properties.getProperty(SERVER_IDLE_TIME)));
        serverInfo.setBufferSize(Integer.parseInt(properties.getProperty(SERVER_BUFFER_SIZE)));
        serverInfo.setCacheSize(Integer.parseInt(properties.getProperty(SERVER_CACHE_SIZE)));

        return serverInfo;
    }
}
