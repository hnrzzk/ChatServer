package com.prefect.chatserver.commoms.util;

/**
 * 命令类型
 * 0x00XX 用户请求
 * 0x01XX 请求响应
 * 0x02XX 服务器通知
 * Created by zhangkai on 2016/12/27.
 */
public class CommandType {

    //消息
    public final static int MESSAGE = 0x0000;
    public final static int MESSAGE_ACK = 0x0100;

    //用户注册
    public final static int USER_SIGN_IN = 0x0001;
    public final static int USER_SIGN_IN_ACK = 0x0101;

    //用户登录
    public final static int USER_LOGIN = 0x0002;
    public final static int USER_LOGIN_ACK = 0x0102;

    //新增好友
    public final static int FRIEND_LIST_ADD = 0x0003;
    public final static int FRIEND_LIST_ADD_ACK = 0x0103;

    //移除好友
    public final static int FRIEND_LIST_REMOVE= 0x0030;
    public final static int FRIEND_LIST_REMOVE_ACK= 0x0105;



    //用户上下线通知
    public final static int USER_ON_LINE_NOTICE= 0x0204;
    public final static int USER_OFF_LINE_NOTICE= 0x0205;

//
//    //好友列表请求
//    public final static int FRIEND_LIST_GET= 0x2010;
//    public final static int FRIEND_LIST_GET_ACK= 0x0003;
//


//
//    //修改分组
//    public final static int FRIEND_LIST_GROUP_CHANGE= 0x2040;
//    public final static int FRIEND_LIST_GROUP_CHANGE_ACK= 0x0006;
//
//    //添加分组
//    public final static int FRIEND_LIST_GROUP_ADD= 0x2050;
//    public final static int FRIEND_LIST_GROUP_ADD_ACK= 0x0007;
//
//    //移除分组
//    public final static int FRIEND_LIST_GROUP_REMOVE= 0x2060;
//    public final static int FRIEND_LIST_GROUP_REMOVE_ACK= 0x0008;
//
//    //黑名单列表请求
//    public final static int BLACK_LIST_GET= 0x3010;
//    public final static int BLACK_LIST_GET_ACK= 0x0010;
//
//    //添加黑名单
//    public final static int BLACK_LIST_ADD= 0x3020;
//    public final static int BLACK_LIST_ADD_ACK= 0x0011;
//
//    //移除黑名单
//    public final static int BLACK_LIST_REMOVE= 0x3030;
//    public final static int BLACK_LIST_REMOVE_ACK= 0x0022;


}
