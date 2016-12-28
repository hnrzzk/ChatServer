package com.prefect.chatserver.client.process;

import com.prefect.chatserver.client.util.ClientCommandType;

/**
 * Created by zhangkai on 2016/12/28.
 */
public class PoFactory {
    public static MessagePo getClass(ClientCommandType commandType) {
        switch (commandType) {
            case Login:
                return new LoginPo();
            case Sigin:
                return new SiginInPo();
            default:
                return null;
        }
    }
}
