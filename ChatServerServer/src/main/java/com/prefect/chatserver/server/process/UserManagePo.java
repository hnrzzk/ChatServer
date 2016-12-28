package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.ChatMessage;
import org.apache.mina.core.session.IoSession;

/**
 * Created by zhangkai on 2016/12/28.
 */
public abstract class UserManagePo implements MessageProcess{
    void response(IoSession ioSession, int commandType,String message){
        MessagePacket messagePacket = new MessagePacket();
        messagePacket.setMessageType(MessageType.MESSAGE);
        messagePacket.setCommand(commandType);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSendAccount("System");
        chatMessage.setMessage(message);

        String json = JSON.toJSONString(chatMessage);
        messagePacket.setMessage(json);
        messagePacket.setMessageLength(json.getBytes().length);

        ioSession.write(messagePacket);
    }
}
