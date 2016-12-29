package com.prefect.chatserver.client.process.request;

import com.prefect.chatserver.commoms.util.MessagePacket;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.mina.core.session.IoSession;

/**
 * 发送消息的处理逻辑
 * Created by zhangkai on 2016/12/29.
 */
public class SendMessagePo implements RequestPo{
    @Override
    public void process(IoSession ioSession, MessagePacket messagePacket) {
        ioSession.write(messagePacket);
    }
}
