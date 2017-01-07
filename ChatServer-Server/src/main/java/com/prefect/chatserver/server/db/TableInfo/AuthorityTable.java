package com.prefect.chatserver.server.db.TableInfo;

/**
 * Created by zhangkai on 2017/1/4.
 */
public class AuthorityTable {
    public static String name = "authority";

    public static int ADMINISTER=0; //管理员
    public static int ORDINARY_USER=1;  //普通用户

    public static class Field {
        public static String id = "id";
        public static String authority = "authority_id";
        public static String name = "name";
    }
}
