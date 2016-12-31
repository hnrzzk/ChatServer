package com.prefect.chatserver.client.process.request;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.ChatMessage;
import org.apache.mina.core.session.IoSession;

/**
 * 发送消息的处理逻辑
 * Created by zhangkai on 2016/12/29.
 */
public class SendMessagePo {

    /**
     * -talk friendAccount message
     * @param command
     */
    public void requestSendMessage(String[] command) {
        MessagePacket messagePacket = new MessagePacket();
        messagePacket.setCommand(CommandType.MESSAGE);
        messagePacket.setMessageType(MessageType.MESSAGE);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSendAccount(ChatClient.account);
        chatMessage.setReceiveAccount(command[1]);
        chatMessage.setMessage(command[2]);

        String json = JSON.toJSONString(chatMessage);
        messagePacket.setMessage(json);
        messagePacket.setMessageLength(json.getBytes().length);

        ChatClient.session.write(messagePacket);
    }
}
