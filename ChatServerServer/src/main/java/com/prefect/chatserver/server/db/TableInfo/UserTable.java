package com.prefect.chatserver.server.db.TableInfo;

/**
 * 数据库User表信息
 * Created by zhangkai on 2016/12/30.
 */
public class UserTable {
    public static String name = "user";

    public static class Field {
        public static String id = "id";
        public static String nickName = "nickName";
        public static String account = "account";
        public static String password = "password";
        public static String registerTime = "register_time";
        public static String sex = "sex";
        public static String isOnline = "is_online";
        public static String isGag="is_gag";
    }
}
