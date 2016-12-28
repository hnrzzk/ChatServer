package com.prefect.chatserver.client.process.response;

import com.prefect.chatserver.commoms.util.CommandType;

/**
 * Created by zhangkai on 2016/12/28.
 */
public class ResponsePoFactory {
    public static ResponsePo getClass(int commandType) {
        switch (commandType) {
            case CommandType.USER_LOGIN_ACK:
            case CommandType.USER_SIGN_IN_ACK:
            case CommandType.MESSAGE:
            case CommandType.MESSAGE_ACK:
                return new MessagePo();
            default:
                 return null;
        }
    }
}