package com.prefect.chatserver.server.process.chatroom;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.moudel.ChatRoomMessage;
import com.prefect.chatserver.server.ChatServer;
import com.prefect.chatserver.server.process.MessageProcess;
import org.apache.mina.core.session.IoSession;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by zhangkai on 2017/1/3.
 */
public abstract class ChatRoomPo implements MessageProcess {

    /**
     * 向聊天室发送消息
     *
     * @param session       发送消息的session
     * @param chatRoomName  聊天室名字
     * @param messagePacket 发送的消息包
     */
    void sendMessage(IoSession session, String chatRoomName, MessagePacket messagePacket) {
        CopyOnWriteArraySet<IoSession> sessionSet = ChatServer.chatRoomInfo.get(chatRoomName);
        if (null != sessionSet) {  //如果聊天室存在
            for (IoSession item : sessionSet) {
                if(item.isConnected()){
                    item.write(messagePacket);
                }
            }
        } else {    //如果聊天室不存在
            ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
            chatRoomMessage.setResult(false);
            chatRoomMessage.setMessage("聊天室不存在");
            chatRoomMessage.setAccount("System");

            String json = JSON.toJSONString(chatRoomMessage);

            messagePacket.setMessageLength(json.getBytes().length);
            messagePacket.setMessage(json);

            session.write(messagePacket);
        }
    }
}
