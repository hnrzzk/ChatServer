package com.prefect.chatserver.server.process.chatroom;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.MessageType;
import com.prefect.chatserver.commoms.utils.moudel.ChatRoomMessage;
import com.prefect.chatserver.server.ChatServer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 进入聊天室处理逻辑
 * Created by zhangkai on 2017/1/3.
 */
public class ChatRoomEnterPo extends ChatRoomPo {
    private final static Logger logger = LoggerFactory.getLogger(ChatRoomEnterPo.class);

    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) {

        ChatRoomMessage chatRoomMessage = JSON.parseObject(messageObj.getMessage(), ChatRoomMessage.class);
        String chatRoomName = chatRoomMessage.getChatRoomName();

        CopyOnWriteArraySet<IoSession> sessionSet;
        //如果聊天室不存在则创建
        if (!ChatServer.chatRoomInfo.containsKey(chatRoomName)) {
            sessionSet = new CopyOnWriteArraySet<>();
            ChatServer.chatRoomInfo.put(chatRoomName, sessionSet);
            logger.info(new StringBuilder()
                    .append("创建聊天室:").append(chatRoomName)
                    .append(" 创建账号:").append(chatRoomMessage.getAccount())
                    .toString());
        } else {
            sessionSet = ChatServer.chatRoomInfo.get(chatRoomName);
        }

        if (sessionSet.contains(ioSession)) {//如果聊天室列表中存在该session
            String value = new StringBuilder()
                    .append("您已经在聊天室中[").append(chatRoomName).append("].")
                    .append(" 退出后才能进入另一个聊天室")
                    .toString();
            MessagePacket messagePacketError = new MessagePacket();
            messagePacketError.setCommand(CommandType.MESSAGE_ACK);
            messagePacketError.setMessageType(MessageType.STRING);
            try {
                messagePacketError.setMessageLength(value.getBytes("utf-8").length);
                messagePacketError.setMessage(value);
                ioSession.write(messagePacketError);
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
            return;
        } else {
            sessionSet.add(ioSession);
        }


        chatRoomMessage.setResult(true);
        chatRoomMessage.setMessage(new StringBuilder()
                .append("用户[").append(chatRoomMessage.getAccount()).append("]")
                .append(" 进入聊天室[").append(chatRoomName).append("].   大家好～～～")
                .toString());

        ioSession.setAttribute("chatRoom", chatRoomName);

        String json = JSON.toJSONString(chatRoomMessage);
        MessagePacket messagePacket = null;
        try {
            messagePacket = new MessagePacket(
                    CommandType.CHAT_ROOM_ENTER_ACK,
                    MessageType.CHATROOM_MANAGE,
                    json.getBytes("utf-8").length,
                    json);
            sendMessage(ioSession, chatRoomName, messagePacket);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
