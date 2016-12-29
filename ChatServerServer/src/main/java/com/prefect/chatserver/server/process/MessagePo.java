package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.moudel.ChatMessage;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import org.apache.mina.core.session.IoSession;

/**
 * Created by zhangkai on 2016/12/29.
 */
public class MessagePo implements MessageProcess {
    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) throws Exception {
        ChatMessage chatMessage = JSON.parseObject(messageObj.getMessage(), ChatMessage.class);

        IoSession receiveSecession= ChatServerHandler.sessionMap.get(chatMessage.getReceiveAccount());
        if (receiveSecession!=null){
            receiveSecession.write(messageObj);
        }
    }
}
