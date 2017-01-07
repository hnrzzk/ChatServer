package com.prefect.chatserver.server.db.TableInfo;

/**
 * 离线消息表
 * Created by hnrzz on 2017/1/7.
 */
public class OfflineMessageTable {
    public static String name = "offline_message";

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
}
