package com.prefect.chatserver.server.db.TableInfo;

/**
 * Created by zhangkai on 2017/1/6.
 */
public class UserGagTable {
    public static String name = "user_gag";

    public static class Field {
        public static String id = "id";
        public static String account = "account";
        public static String reason = "reason";
        public static String startTime = "start_time";
        public static String endTimne = "end_time";
        public static String cancel = "CANCEL";
        public static String cancelTime = "cancel_time";
    }

    public static class Status {
        public static int GAG = 0;
        public static int CANCEL = 1;
    }
}
