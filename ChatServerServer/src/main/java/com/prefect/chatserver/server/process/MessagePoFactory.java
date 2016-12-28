package com.prefect.chatserver.server.process;

import com.prefect.chatserver.commoms.util.CommandType;
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
            case CommandType.USER_LOGIN:
                return new LoginPo();
            case CommandType.USER_SIGN_IN:
                return new SignInPo();
        }
        return messageProcess;
    }
}
