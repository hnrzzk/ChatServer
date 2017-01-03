package com.prefect.chatserver.client.process.response;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.ChatRoomMessage;

/**
 * Created by zhangkai on 2017/1/3.
 */
public class ChatRoomEnterAckResponsePo extends ChatRoomResponsePo{
    @Override
    public void process(MessagePacket messagePacket){
        super.process(messagePacket);
        if (MessageType.CHATROOM_MANAGE == messagePacket.getMessageType()) {
            ChatRoomMessage chatRoomMessage = JSON.parseObject(messagePacket.getMessage(), ChatRoomMessage.class);
            if (chatRoomMessage.getResult()) {
                //服务器返回加入聊天室成功则在服务器上记录聊天室信息
                ChatClient.chatRoomName = chatRoomMessage.getChatRoomName();
            }
        }
    }
}
