package com.prefect.chatserver.client.util;

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
            File directory=new File("ChatClientConfig.properties");
            System.out.println(directory.getAbsolutePath());

            FileInputStream fileInputStream=new FileInputStream(filePath);
            properties.load(fileInputStream);
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
