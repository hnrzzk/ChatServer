package com.prefect.chatserver.client.process.response;

import com.prefect.chatserver.commoms.utils.CommandType;

/**
 * Created by zhangkai on 2016/12/28.
 */
public class ResponsePoFactory {
    public static ResponsePo getClass(int commandType) {
        switch (commandType) {
            case CommandType.MESSAGE:
                return new MessagePo();

            case CommandType.MESSAGE_ACK:
            case CommandType.FRIEND_LIST_ADD_ACK:
            case CommandType.FRIEND_LIST_REMOVE_ACK:
            case CommandType.USER_ON_LINE_NOTICE:
            case CommandType.USER_OFF_LINE_NOTICE:
            case CommandType.BLACK_LIST_ADD_ACK:
            case CommandType.BLACK_LIST_REMOVE_ACK:
            case CommandType.SEND_BROADCAST_ACK:
            case CommandType.USER_GAG_ACK:
            case CommandType.USER_GAG_CANCEL_ACK:
            case CommandType.USER_NO_LOGIN_ACK:
            case CommandType.USER_NO_LOGIN_CANCEL_ACK:
                return new ActionResponsePo();

            case CommandType.FRIEND_LIST_ADD:
                return new FriendAddResponsePo();

            case CommandType.CHAT_ROOM_ENTER_ACK:
                return new ChatRoomEnterAckResponsePo();

            case CommandType.CHAT_ROOM_QUIT_ACK:
            case CommandType.CHAT_ROOM_SEND_ACK:
                return new ChatRoomResponsePo();

            case CommandType.USER_FIND_ACK:
                return new FindFriendListResponsePo();

            case CommandType.USER_LOGIN_REQUEST_ACK:
                return new LoginResponsePo();

            case CommandType.USER_SIGN_IN_ACK:
            case CommandType.USER_LOGIN_VERIFY_ACK:
                return new LoginVerifyResponsePo();
            default:
                return null;
        }
    }
}