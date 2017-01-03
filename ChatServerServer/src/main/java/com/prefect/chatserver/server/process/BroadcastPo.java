package com.prefect.chatserver.server.process;

import com.prefect.chatserver.commoms.util.MessagePacket;
import org.apache.mina.core.session.IoSession;

/**
 * 发送广播消息的处理逻辑
 * Created by zhangkai on 2017/1/3.
 */
public class BroadcastPo extends ActionPo{
    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) {

    }
}
