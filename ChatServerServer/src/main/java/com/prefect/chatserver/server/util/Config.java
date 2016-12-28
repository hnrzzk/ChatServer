package com.prefect.chatserver.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by zhangkai on 2016/12/27.
 */
public class Config {
    String filePath;

    private final String SERVER_PORT = "server.port";
    private final String SERVER_TIME_OUT = "server.timeOut";
    private final String SERVER_IDLE_TIME = "server.IdleTime";
    private final String SERVER_BUFFER_SIZE = "server.BufferSize";

    public Config() {
        this("ChatServerConfig.properties");
    }

    public Config(String filePath) {
        this.filePath = filePath;
    }

    public ServerInfo getServerConf() throws Exception {

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
