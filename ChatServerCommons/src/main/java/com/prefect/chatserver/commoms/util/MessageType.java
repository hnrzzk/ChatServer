package com.prefect.chatserver.commoms.util;

/**
 * Created by zhangkai on 2016/12/27.
 */
public class MessageType {
    //好友管理消息 对应FriendInfo
    public static final int FRIEND_MANAGE = 0x0002;

    //用户管理消息 对应 UserInfo
    public static final int USER_MANAGE = 0x0003;

    //聊天消息 对应  ChatMessage
    public static final int MESSAGE = 0x0004;

    //登录消息 对应 LoginMessage
    public static final int LOGIN_MESSAGE = 0x0005;

    //请求响应结果 对应ActionResponseMessage
    public static final int RESPONSE = 0x0006;
}
