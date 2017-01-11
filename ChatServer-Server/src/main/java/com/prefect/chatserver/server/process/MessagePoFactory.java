package com.prefect.chatserver.server.process;

import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.server.process.administer.BroadcastPo;
import com.prefect.chatserver.server.process.administer.UserAuthorityManagePo;
import com.prefect.chatserver.server.process.chatroom.ChatRoomEnterPo;
import com.prefect.chatserver.server.process.chatroom.ChatRoomQuit;
import com.prefect.chatserver.server.process.chatroom.ChatRoomSend;
import com.prefect.chatserver.server.process.relationship.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 业务处理类工厂
 * Created by zhangkai on 2016/12/27.
 */
public class MessagePoFactory {
    static Map<Integer, MessageProcess> messageProcessMap = new ConcurrentHashMap<>();

    public static MessageProcess getClass(int commandType) {
        MessageProcess messageProcess;
        if (messageProcessMap.containsKey(commandType)) {
            messageProcess = messageProcessMap.get(commandType);
        } else {
            synchronized (MessagePoFactory.class) {
                if (!messageProcessMap.containsKey(commandType)){
                    messageProcess = getMessageProcess(commandType);
                    if (messageProcess != null) {
                        messageProcessMap.put(commandType, messageProcess);
                    }
                }else{
                    messageProcess = messageProcessMap.get(commandType);
                }
            }
        }
        return messageProcess;
    }

    private static MessageProcess getMessageProcess(int commandType) {
        switch (commandType) {
            case CommandType.USER_LOGIN_REQUEST:    //用户登录请求
                return new LoginRequestPo();
            case CommandType.USER_LOGIN_VERIFY:    //用户登录验证
                return new LoginVerifyPo();
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
            case CommandType.USER_GAG:
            case CommandType.USER_GAG_CANCEL:
            case CommandType.USER_NO_LOGIN:
            case CommandType.USER_NO_LOGIN_CANCEL:
                return new UserAuthorityManagePo();
            case CommandType.USER_FIND:
                return new FindUser();
            default:
                return null;
        }
    }

}
