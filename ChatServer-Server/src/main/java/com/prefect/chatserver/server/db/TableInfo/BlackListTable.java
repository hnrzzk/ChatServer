package com.prefect.chatserver.server.db.TableInfo;

/**
 * 黑名单表 信息
 * Created by zhangkai on 2017/1/3.
 */
public class BlackListTable {
    public static String name = "black_list";

    public static class Field {
        public static String id = "id";
        public static String userAccount = "user_account";
        public static String blackAccount = "black_account";
        public static String createTime = "create_time";
    }
}
