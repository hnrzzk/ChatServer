package com.prefect.chatserver.client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by zhangkai on 2016/12/27.
 */
public class Config {
    Properties properties;

    private final String SERVER_HOST_NAME = "server.hostname";
    private final String SERVER_PORT = "server.port";

    public Config() {
        this("ChatClientConfig.properties");
    }

    public Config(String filePath) {
        properties = new Properties();
        try {
            properties.load(Config.class.getClassLoader().getResourceAsStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerInfo getServerConf() {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setHostName(properties.getProperty(SERVER_HOST_NAME));
        serverInfo.setPort(Integer.parseInt(properties.getProperty(SERVER_PORT)));

        return serverInfo;
    }
}
