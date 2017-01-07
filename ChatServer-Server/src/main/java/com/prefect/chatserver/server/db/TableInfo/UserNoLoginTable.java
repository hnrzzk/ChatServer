package com.prefect.chatserver.server.db.TableInfo;

/**
 * 禁言表user_gag信息
 * Created by zhangkai on 2017/1/6.
 */
public class UserNoLoginTable {
    public static String name = "user_no_login";

    public static class Field {
        public static String id = "id";
        public static String account = "account";
        public static String reason = "reason";
        public static String startTime = "start_time";
        public static String endTime = "end_time";
        public static String cancel = "CANCEL";
        public static String cancelTime = "cancel_time";
    }

    public static class Status {
        public static int NO_LOGIN = 0;
        public static int CANCEL = 1;
    }
}
