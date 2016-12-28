package com.prefect.chatserver.commoms.util;

/**
 * Created by zhangkai on 2016/12/27.
 */
public class CommandType {

    //用户注册
    public final static int USER_SIGN_IN = 0x1000;
    public final static int USER_SIGN_IN_ACK= 0x1001;

    //用户登录
    public final static int USER_LOGIN= 0x1010;
    public final static int USER_LOGIN_ACK= 0x1011;

    //用户上下线通知
    public final static int USER_ON_LINE_NOTICE= 0x1020;
    public final static int USER_OFF_LINE_NOTICE= 0x1030;

    //好友列表请求
    public final static int FRIEND_LIST_GET= 0x2010;
    public final static int FRIEND_LIST_GET_ACK= 0x2011;

    //新增好友
    public final static int FRIEND_LIST_ADD= 0x2020;
    public final static int FRIEND_LIST_ADD_ACK= 0x2021;
    //移除好友
    public final static int FRIEND_LIST_REMOVE= 0x2030;
    public final static int FRIEND_LIST_REMOVE_ACK= 0x2031;

    //修改分组
    public final static int FRIEND_LIST_GROUP_CHANGE= 0x2040;
    public final static int FRIEND_LIST_GROUP_CHANGE_ACK= 0x2041;

    //添加分组
    public final static int FRIEND_LIST_GROUP_ADD= 0x2050;
    public final static int FRIEND_LIST_GROUP_ADD_ACK= 0x2051;

    //移除分组
    public final static int FRIEND_LIST_GROUP_REMOVE= 0x2060;
    public final static int FRIEND_LIST_GROUP_REMOVE_ACK= 0x2061;

    //好友申请
    public final static int FRIEND_LIST_FIND= 0x2070;
    public final static int FRIEND_LIST_FIND_ACK= 0x2071;

    //黑名单列表请求
    public final static int BLACK_LIST_GET= 0x3010;
    public final static int BLACK_LIST_GET_ACK= 0x3011;

    //添加黑名单
    public final static int BLACK_LIST_ADD= 0x3020;
    public final static int BLACK_LIST_ADD_ACK= 0x3021;

    //移除黑名单
    public final static int BLACK_LIST_REMOVE= 0x3030;
    public final static int BLACK_LIST_REMOVE_ACK= 0x3031;

    //消息
    public final static int MESSAGE= 0x4000;
    public final static int MESSAGE_ACK= 0x4001;
}
