package com.prefect.chatserver.server.process;

import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.server.process.chatroom.ChatRoomEnterPo;
import com.prefect.chatserver.server.process.chatroom.ChatRoomQuit;
import com.prefect.chatserver.server.process.chatroom.ChatRoomSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务处理类工厂
 * Created by zhangkai on 2016/12/27.
 */
public class MessagePoFactory {
    private final static Logger logger = LoggerFactory.getLogger(MessagePoFactory.class);

    public static MessageProcess getClass(int commandType) {
        MessageProcess messageProcess = null;
        switch (commandType) {
            case CommandType.USER_LOGIN:    //用户登录
                return new LogInPo();
            case CommandType.USER_SIGN_IN:  //用户注册
                return new SignInPo();
            case CommandType.MESSAGE:   //消息信息
            case CommandType.FRIEND_LIST_ADD:   //增加好友
                return new MessagePo();
            case CommandType.FRIEND_LIST_ADD_ACK:   //好友请求响应
                return new FriendAddACKPo();
            case CommandType.FRIEND_LIST_REMOVE:    //清除好友
                return new FriendRemovePo();
            case CommandType.BLACK_LIST_ADD:    //增加黑名单
                return new BlackListAddPo();
            case CommandType.BLACK_LIST_REMOVE: //移除黑名单
                return new BlackListRemovePo();
            case CommandType.CHAT_ROOM_ENTER:   //用户请求进入聊天室
                return new ChatRoomEnterPo();
            case CommandType.CHAT_ROOM_SEND:    //用户请求发送消息
                return new ChatRoomSend();
            case CommandType.CHAT_ROOM_QUIT:    //用户请求退出聊天室
                return new ChatRoomQuit();
            case CommandType.SEND_BROADCAST:    //用户请求发送广播
                return new BroadcastPo();
        }
        return messageProcess;
    }
}
