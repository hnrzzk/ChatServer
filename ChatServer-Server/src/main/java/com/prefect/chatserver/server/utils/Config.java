package com.prefect.chatserver.server.utils;

import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置类
 * Created by zhangkai on 2016/12/27.
 */
public class Config {
    private String systemConfigFilePath = "ChatServerConfig.properties";
    private String log4jConfigFilePath = "log4j.properties";

    private String configDirPath = "";

    public Config() {
        this("../config/");
    }

    public Config(String configDirPath) {
        this.configDirPath = configDirPath;
    }

    public ServerInfo getServerConf() throws Exception {
        String SERVER_PORT = "server.port";
        String SERVER_TIME_OUT = "server.timeOut";
        String SERVER_IDLE_TIME = "server.IdleTime";
        String SERVER_BUFFER_SIZE = "server.bufferSize";
        String SERVER_HOSTNAME = "server.hostname";
        String SERVER_CACHE_SIZE = "server.cacheSize";

        //读取log4j配置文件
        InputStream log4jConfigInputStream;
        try {
            log4jConfigInputStream = new FileInputStream(configDirPath + log4jConfigFilePath);
        } catch (Exception e) {
            log4jConfigInputStream = Config.class.getClassLoader().getResourceAsStream(log4jConfigFilePath);
        }
        PropertyConfigurator.configure(log4jConfigInputStream);

        Properties properties = new Properties();

        //读取系统配置文件
        InputStream appConfigInputStream;
        try {
            appConfigInputStream = new FileInputStream(configDirPath + systemConfigFilePath);
        } catch (Exception e) {
            appConfigInputStream = Config.class.getClassLoader().getResourceAsStream(systemConfigFilePath);
        }
        properties.load(appConfigInputStream);

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
