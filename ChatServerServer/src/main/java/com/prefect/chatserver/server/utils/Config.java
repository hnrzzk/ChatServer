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

        File file=new File("ChatServerConfig.properties");
        System.out.println(file.getAbsolutePath());

        Properties properties = new Properties();
        properties.load(new FileInputStream(filePath));

        ServerInfo serverInfo = new ServerInfo();

        serverInfo.setPort(Integer.parseInt(properties.getProperty(SERVER_PORT)));
        serverInfo.setTimeOut(Integer.parseInt(properties.getProperty(SERVER_TIME_OUT)));
        serverInfo.setIdleTime(Integer.parseInt(properties.getProperty(SERVER_IDLE_TIME)));
        serverInfo.setBufferSize(Integer.parseInt(properties.getProperty(SERVER_BUFFER_SIZE)));

        return serverInfo;
    }
}