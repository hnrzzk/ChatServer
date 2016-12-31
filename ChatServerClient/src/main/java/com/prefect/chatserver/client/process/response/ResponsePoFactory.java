package com.prefect.chatserver.client.process.response;

import com.prefect.chatserver.commoms.util.CommandType;

/**
 * Created by zhangkai on 2016/12/28.
 */
public class ResponsePoFactory {
    public static ResponsePo getClass(int commandType) {
        switch (commandType) {
            case CommandType.MESSAGE:
                return new MessagePo();
            case CommandType.USER_LOGIN_ACK:
            case CommandType.USER_SIGN_IN_ACK:
            case CommandType.MESSAGE_ACK:
            case CommandType.FRIEND_LIST_ADD_ACK:
            case CommandType.FRIEND_LIST_REMOVE_ACK:
            case CommandType.USER_ON_LINE_NOTICE:
            case CommandType.USER_OFF_LINE_NOTICE:
                return new ActionResponsePo();
            case CommandType.FRIEND_LIST_ADD:
                return new FriendAddResponsePo();
            default:
                return null;
        }
    }
}