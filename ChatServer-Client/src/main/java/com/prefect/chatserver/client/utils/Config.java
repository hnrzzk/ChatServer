package com.prefect.chatserver.client.utils;

import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zhangkai on 2016/12/27.
 */
public class Config {
    Properties properties;

    private final String SERVER_HOST_NAME = "server.hostname";
    private final String SERVER_PORT = "server.port";

    private String systemConfigFile = "ChatClientConfig.properties";
    private String log4jConfigFile = "log4j.properties";

    public Config() {
        properties = new Properties();

        //读取log4j配置文件
        InputStream log4jConfigInputStream;
        try {
            log4jConfigInputStream = new FileInputStream("../config/" + log4jConfigFile);
        } catch (Exception e) {
            log4jConfigInputStream = Config.class.getClassLoader().getResourceAsStream(log4jConfigFile);
        }
        PropertyConfigurator.configure(log4jConfigInputStream);

        //读取系统配置文件
        InputStream appConfigInputStream;
        try {
            appConfigInputStream = new FileInputStream("../config/" + systemConfigFile);
        } catch (Exception e) {
            appConfigInputStream = Config.class.getClassLoader().getResourceAsStream(systemConfigFile);
        }

        try {
            properties.load(appConfigInputStream);
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
