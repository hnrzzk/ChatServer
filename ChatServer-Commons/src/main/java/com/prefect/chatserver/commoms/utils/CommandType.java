package com.prefect.chatserver.commoms.utils;

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

    //用户登录请求
    public final static int USER_LOGIN_REQUEST = 0x0002;
    public final static int USER_LOGIN_REQUEST_ACK = 0x0102;

    //用户登录验证
    public final static int USER_LOGIN_VERIFY = 0x0010;
    public final static int USER_LOGIN_VERIFY_ACK = 0x0110;

    //新增好友
    public final static int FRIEND_LIST_ADD = 0x0003;
    public final static int FRIEND_LIST_ADD_ACK = 0x0103;

    //移除好友
    public final static int FRIEND_LIST_REMOVE = 0x0004;
    public final static int FRIEND_LIST_REMOVE_ACK = 0x0104;

    //添加黑名单
    public final static int BLACK_LIST_ADD = 0x0005;
    public final static int BLACK_LIST_ADD_ACK = 0x0105;

    //移除黑名单
    public final static int BLACK_LIST_REMOVE = 0x0006;
    public final static int BLACK_LIST_REMOVE_ACK = 0x0106;

    //用户上下线通知
    public final static int USER_ON_LINE_NOTICE = 0x0204;
    public final static int USER_OFF_LINE_NOTICE = 0x0205;

    //用户进入聊天室
    public final static int CHAT_ROOM_ENTER = 0x0007;
    public final static int CHAT_ROOM_ENTER_ACK = 0x0107;

    //用户退出聊天室
    public final static int CHAT_ROOM_QUIT = 0x0008;
    public final static int CHAT_ROOM_QUIT_ACK = 0x0108;

    //用户发送聊天室消息
    public final static int CHAT_ROOM_SEND = 0x0009;
    public final static int CHAT_ROOM_SEND_ACK = 0x0109;

    //用户发送广播请求
    public final static int SEND_BROADCAST = 0x000A;
    public final static int SEND_BROADCAST_ACK=0x010A;

    //用户禁言
    public final static int USER_GAG=0x000B;
    public final static int USER_GAG_ACK=0x010B;

    //用户禁言取消
    public final static int USER_GAG_CANCEL=0x000C;
    public final static int USER_GAG_CANCEL_ACK=0x010C;

    //禁封用户登录
    public final static int USER_NO_LOGIN=0x000D;
    public final static int USER_NO_LOGIN_ACK=0x010D;

    //禁封用户登录取消
    public final static int USER_NO_LOGIN_CANCEL=0x000E;
    public final static int USER_NO_LOGIN_CANCEL_ACK=0x010E;

    //查找用户
    public final static int USER_FIND=0x000F;
    public final static int USER_FIND_ACK=0x010F;

//    //好友列表请求
//    public final static int FRIEND_LIST_GET= 0x2010;
//    public final static int FRIEND_LIST_GET_ACK= 0x0003;
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


}
