package com.prefect.chatserver.server.process.chatroom;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.moudel.ChatRoomMessage;
import org.apache.mina.core.session.IoSession;

/**
 * 向聊天室发送消息
 * Created by zhangkai on 2017/1/3.
 */
public class ChatRoomSend extends ChatRoomPo {
    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) {
        ChatRoomMessage chatRoomMessage = JSON.parseObject(messageObj.getMessage(), ChatRoomMessage.class);

        String chatRoomName = chatRoomMessage.getChatRoomName();

        messageObj.setCommand(CommandType.CHAT_ROOM_SEND_ACK);

        sendMessage(ioSession, chatRoomName, messageObj);
    }

}
