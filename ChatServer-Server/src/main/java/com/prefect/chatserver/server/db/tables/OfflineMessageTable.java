package com.prefect.chatserver.server.db.tables;

import java.sql.Timestamp;

/**
 * 离线消息表
 * Created by hnrzz on 2017/1/7.
 */
public class OfflineMessageTable {

    public static class Field {
        public static String id = "id";
        public static String account = "account";
        public static String commandType = "command_type";
        public static String messageType = "message_type";
        public static String message = "message";
        public static String createTime = "create_time";
        public static String isSend = "is_send";
    }

    public static class Status {
        public static int SEND = 1;
        public static int NOT_SEND = 0;
    }

    long id;
    String account;
    int commandType;
    int messageType;
    String message;
    Timestamp createTime;
    int isSend;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getCommandType() {
        return commandType;
    }

    public void setCommandType(int commandType) {
        this.commandType = commandType;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }
}
