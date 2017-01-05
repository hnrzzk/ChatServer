package com.prefect.chatserver.server.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * 读取配置类
 * Created by zhangkai on 2016/12/27.
 */
public class Config {
    private String filePath;

    public Config() {
        this("ChatServerConfig.properties");
    }

    public Config(String filePath) {
        this.filePath = filePath;
    }

    public ServerInfo getServerConf() throws Exception {
        String SERVER_PORT = "server.port";
        String SERVER_TIME_OUT = "server.timeOut";
        String SERVER_IDLE_TIME = "server.IdleTime";
        String SERVER_BUFFER_SIZE = "server.BufferSize";

        ClassLoader classLoader = Config.class.getClassLoader();
        if (classLoader != null) {
            System.out.println("项目路径：" + classLoader.getResource("ChatServerConfig.properties").toString());
        } else {
            System.out.println("项目路径：" + ClassLoader.getSystemResource("ChatServerConfig.properties").getPath());
        }

        Properties properties = new Properties();
        properties.load(Config.class.getClassLoader().getResourceAsStream("ChatServerConfig.properties"));

        ServerInfo serverInfo = new ServerInfo();

        serverInfo.setPort(Integer.parseInt(properties.getProperty(SERVER_PORT)));
        serverInfo.setTimeOut(Integer.parseInt(properties.getProperty(SERVER_TIME_OUT)));
        serverInfo.setIdleTime(Integer.parseInt(properties.getProperty(SERVER_IDLE_TIME)));
        serverInfo.setBufferSize(Integer.parseInt(properties.getProperty(SERVER_BUFFER_SIZE)));

        return serverInfo;
    }
}
