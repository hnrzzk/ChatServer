package com.prefect.chatserver.commoms.utils;

/**
 * Created by zhangkai on 2016/12/27.
 */
public class MessageType {
    /**
     * 好友管理消息 对应RelationShipMessage
     */
    public static final int RELATIONSHIP_MANAGE = 0x0002;

    /**
     * 用户管理消息 对应 UserInfo
     */
    public static final int USER_INFO = 0x0003;

    /**
     * 聊天消息 对应  ChatMessage
     */
    public static final int MESSAGE = 0x0004;

    /**
     * 登录消息 对应 LoginMessage
     */
    public static final int LOGIN_MESSAGE = 0x0005;

    /**
     * 请求响应结果 对应ActionResponseMessage
     */
    public static final int RESPONSE = 0x0006;

    /**
     * 字符串
     */
    public static final int STRING =0x0007;

    /**
     * 聊天室消息 对应ChatRoomMessage
     */
    public static final int CHATROOM_MANAGE =0x0008;

    /**
     * 用户权限管理 对应 UserAuthorityManageMessage
     */
    public static final int USER_AUTHORITY_MANAGE=0x0009;

    /**
     * 用户列表 对应UserListInfo
     */
    public static final int USER_LIST=0x000A;
}
