package com.prefect.chatserver.client.process.request.operate;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.MessageType;
import com.prefect.chatserver.commoms.utils.moudel.ChatMessage;

/**
 * 发送消息的处理逻辑
 * Created by zhangkai on 2016/12/29.
 */
public class SendMessagePo extends OperatePo {

    public SendMessagePo(String[] strings) {
        super(strings);
    }

    /**
     * -talk friendAccount message
     */
    @Override
    public void process() {
        MessagePacket messagePacket = new MessagePacket();
        messagePacket.setCommand(CommandType.MESSAGE);
        messagePacket.setMessageType(MessageType.MESSAGE);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSendAccount(ChatClient.account);
        chatMessage.setReceiveAccount(super.strings[1]);
        chatMessage.setMessage(super.strings[2]);

        String json = JSON.toJSONString(chatMessage);
        messagePacket.setMessage(json);
        messagePacket.setMessageLength(json.getBytes().length);

        ChatClient.session.write(messagePacket);

    }
}
