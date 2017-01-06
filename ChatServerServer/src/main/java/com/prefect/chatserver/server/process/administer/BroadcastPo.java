package com.prefect.chatserver.server.process.administer;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.moudel.ChatMessage;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import org.apache.mina.core.session.IoSession;

import java.util.Map;

/**
 * 发送广播消息的处理逻辑
 * Created by zhangkai on 2017/1/3.
 */
public class BroadcastPo extends AdministerPo {
    @Override
    public void process(IoSession ioSession, MessagePacket messagePacket) {
        ChatMessage chatMessage = JSON.parseObject(messagePacket.getMessage(), ChatMessage.class);

        if (authorizationCheck(ioSession, chatMessage.getSendAccount())) {
            broadcast(messagePacket);
        }
    }


    /**
     * 向所有在线用户发送广播
     *
     * @param messagePacket
     */
    void broadcast(MessagePacket messagePacket) {
        for (Map.Entry<String, IoSession> entry : ChatServerHandler.sessionMap.entrySet()) {
            messagePacket.setCommand(CommandType.SEND_BROADCAST_ACK);
            IoSession ioSession = entry.getValue();
            if (ioSession.isConnected()) {
                ioSession.write(messagePacket);
            }
        }
    }
}
