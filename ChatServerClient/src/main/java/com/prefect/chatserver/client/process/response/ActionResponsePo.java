package com.prefect.chatserver.client.process.response;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.utils.Interactive;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.MessageType;
import com.prefect.chatserver.commoms.utils.moudel.ACKMessage;
import com.prefect.chatserver.commoms.utils.moudel.ChatMessage;

/**
 * 服务器响应处理逻辑
 * Created by zhangkai on 2016/12/29.
 */
public class ActionResponsePo implements ResponsePo{


    @Override
    public void process(MessagePacket messagePacket) {
        switch (messagePacket.getMessageType()){
            case MessageType.RESPONSE:
                ACKMessage ACKMessage = JSON.parseObject(messagePacket.getMessage(),ACKMessage.class);
                Interactive.getInstance().printlnToConsole(String.format("System:\n    %s", ACKMessage.getMessage()));
                break;
            case MessageType.STRING:
                Interactive.getInstance().printlnToConsole("System:\n    "+messagePacket.getMessage().toString());
                break;
            case MessageType.MESSAGE:
                ChatMessage chatMessage= JSON.parseObject(messagePacket.getMessage(),ChatMessage.class);
                Interactive.getInstance().printlnToConsole(chatMessage.getSendAccount()+":\n    "+chatMessage.getMessage());
        }

    }
}
