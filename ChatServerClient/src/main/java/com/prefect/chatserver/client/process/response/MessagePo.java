package com.prefect.chatserver.client.process.response;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.util.Interactive;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.ChatMessage;

/**
 * 响应消息处理逻辑
 * 消息类型：消息
 * Created by zhangkai on 2016/12/28.
 */
public class MessagePo implements ResponsePo{
    @Override
    public void process(MessagePacket messagePacket) {
        switch (messagePacket.getMessageType()){
            case MessageType.MESSAGE:
                ChatMessage chatMessage = JSON.parseObject(messagePacket.getMessage(),ChatMessage.class);
                Interactive.getInstance().printlnToConsole(String.format("%s:\n%s", chatMessage.getSendAccount(), chatMessage.getMessage()));
        }

    }
}
