package com.prefect.chatserver.server.process.chatroom;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.ChatRoomMessage;
import com.prefect.chatserver.server.ChatServer;
import org.apache.mina.core.session.IoSession;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 聊天室退出逻辑
 * Created by zhangkai on 2017/1/3.
 */
public class ChatRoomQuit extends ChatRoomPo {
    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) {

        ChatRoomMessage chatRoomMessage = JSON.parseObject(messageObj.getMessage(), ChatRoomMessage.class);

        String chatRoomName = chatRoomMessage.getChatRoomName();
        String account = chatRoomMessage.getAccount();

        ChatRoomMessage chatRoomMessageSystem=new ChatRoomMessage();
        chatRoomMessageSystem.setAccount("System");
        chatRoomMessageSystem.setChatRoomName(chatRoomName);

        //聊天室
        if (!ChatServer.chatRoomInfo.containsKey(chatRoomName)){
            chatRoomMessageSystem.setResult(false);
            chatRoomMessageSystem.setMessage(new StringBuilder()
                    .append(" 聊天室[").append(chatRoomName).append("] 不存在!")
                    .toString());
        }else {
            chatRoomMessageSystem.setResult(true);
            chatRoomMessageSystem.setMessage(new StringBuilder()
                    .append("用户[").append(account).append("] 离开聊天室!")
                    .toString());

            //根据聊天室名字找到聊天室用户列表
            CopyOnWriteArraySet<IoSession> ioSessionsList=ChatServer.chatRoomInfo.get(chatRoomName);
            //将该session从用户列表中移除
            ioSessionsList.remove(ioSession);
        }

        MessagePacket messagePacket=new MessagePacket();
        messagePacket.setCommand(CommandType.CHAT_ROOM_QUIT_ACK);
        messagePacket.setMessageType(MessageType.CHATROOM_MANAGE);

        String json= JSON.toJSONString(chatRoomMessageSystem);
        messagePacket.setMessageLength(json.getBytes().length);
        messagePacket.setMessage(json);

        sendMessage(ioSession,chatRoomName,messagePacket);
    }
}
