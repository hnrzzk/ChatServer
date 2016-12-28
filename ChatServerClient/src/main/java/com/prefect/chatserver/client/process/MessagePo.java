package com.prefect.chatserver.client.process;

import com.prefect.chatserver.commoms.util.ChatMessage;
import org.apache.mina.core.session.IoSession;

/**
 * Created by zhangkai on 2016/12/28.
 */
public interface MessagePo {
    void process(IoSession ioSession, ChatMessage chatMessage);
}
