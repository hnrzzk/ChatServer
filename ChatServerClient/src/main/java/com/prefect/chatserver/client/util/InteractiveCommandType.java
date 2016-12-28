package com.prefect.chatserver.client.util;

/**
 * 用户与客户端交互命令类型
 * Created by zhangkai on 2016/12/28.
 */
public enum InteractiveCommandType {
    INIT,   //初始化
    SIGNIN, //登录
    LOGIN,  //注册
    FRIEND_FIND,    //查找好友
    FRIEND_ADD,     //增加好友
    FRIEND_REMOVE,  //
    SEND_MESSAGE

}
