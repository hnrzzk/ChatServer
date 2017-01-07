package com.prefect.chatserver.server.db.TableInfo;

/**
 * 数据库friends表信息
 * Created by zhangkai on 2016/12/30.
 */
public class FriendsTable {
    public static String name = "friends";

    public static class Field {
        public static String id = "id";
        public static String userAccount = "user_account";
        public static String friendAccount = "friend_account";
        public static String categoryId = "category_id";
        public static String createTime = "create_time";
    }
}
