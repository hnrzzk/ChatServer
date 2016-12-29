package com.prefect.chatserver.client.process.request;

import com.prefect.chatserver.commoms.util.MessagePacket;
import org.apache.mina.core.session.IoSession;

/**
 * Created by zhangkai on 2016/12/29.
 */
public class FriendManagePo implements RequestPo {
    @Override
    public void process(IoSession ioSession, MessagePacket messagePacket) {
        ioSession.write(messagePacket);
    }
}
