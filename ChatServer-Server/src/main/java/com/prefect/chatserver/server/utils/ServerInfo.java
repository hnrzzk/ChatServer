package com.prefect.chatserver.server.utils;

/**
 * Created by zhangkai on 2016/12/27.
 */
public class ServerInfo {
    private String hostname = "localhost";
    private int port = 9123;   //端口号
    private int timeOut = 30;    //超时时间
    private int IdleTime = 30;
    private int bufferSize = 2048;
    private int cacheSize = 4000;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public int getIdleTime() {
        return IdleTime;
    }

    public void setIdleTime(int idleTime) {
        IdleTime = idleTime;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        bufferSize = bufferSize;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }
}
