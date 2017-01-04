package com.prefect.chatserver.client.process.request.operate;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.ChatMessage;

/**
 * 发送广播消息请求
 * Created by zhangkai on 2017/1/3.
 */
public class BroadCastPo extends OperatePo {

    public BroadCastPo(String[] strings) {
        super(strings);
    }

    @Override
    public void process() {

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < strings.length; i++) {
            stringBuilder.append(strings[i]).append(" ");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        MessagePacket messagePacket = new MessagePacket();
        messagePacket.setCommand(CommandType.SEND_BROADCAST);
        messagePacket.setMessageType(MessageType.MESSAGE);

        String message = stringBuilder.toString();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSendAccount(ChatClient.account);
        chatMessage.setMessage(message);

        String json = JSON.toJSONString(chatMessage);

        messagePacket.setMessage(json);
        messagePacket.setMessageLength(json.getBytes().length);

        ChatClient.session.write(messagePacket);
    }
}
