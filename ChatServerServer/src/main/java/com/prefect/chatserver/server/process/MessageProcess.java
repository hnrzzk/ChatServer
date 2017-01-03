package com.prefect.chatserver.server.process;

import com.prefect.chatserver.commoms.util.MessagePacket;
import org.apache.mina.core.session.IoSession;



/**
 * 消息处理的基类
 * Created by zhangkai on 2016/12/27.
 */
public interface MessageProcess {
    void process(IoSession ioSession, MessagePacket messageObj);
}
