package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.ChatMessage;
import com.prefect.chatserver.commoms.util.moudel.FriendManageMessage;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import com.prefect.chatserver.server.db.DBDao;
import org.apache.mina.core.session.IoSession;

/**
 * Created by zhangkai on 2016/12/29.
 */
public class MessagePo extends ActionPo {
    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) throws Exception {
        switch (messageObj.getMessageType()) {
            case MessageType.MESSAGE:
                sendChatMessage(ioSession, messageObj);
            case MessageType.FRIEND_MANAGE:
                sendFriendAddRequest(ioSession, messageObj);
        }
    }

    /**
     * 发送聊天数据
     */
    private void sendChatMessage(IoSession ioSession, MessagePacket messageObj) {
        ChatMessage chatMessage = JSON.parseObject(messageObj.getMessage(), ChatMessage.class);

        IoSession receiveSecession = ChatServerHandler.sessionMap.get(chatMessage.getReceiveAccount());
        if (receiveSecession != null) { //如果好友在线则发送消息
            receiveSecession.write(messageObj);
        } else {
            //TODO:存储离线消息
        }
    }

    /**
     * 发送好友请求数据
     */
    private void sendFriendAddRequest(IoSession ioSession, MessagePacket messageObj) {
        FriendManageMessage friendManageMessage = JSON.parseObject(messageObj.getMessage(), FriendManageMessage.class);

        String friendAccount = friendManageMessage.getFriendAccount();

        //判断是否有该账户
        if (!DBDao.getInstance().accountIsExist(friendAccount)) {
            response(ioSession, CommandType.FRIEND_LIST_ADD_ACK, false, "friendAccount does not exist!");
            return;
        }

        IoSession receiveSecession = ChatServerHandler.sessionMap.get(friendAccount);
        //判断该账户是否在线
        if (receiveSecession != null) { //如果好友在线则发送消息
            receiveSecession.write(messageObj);
        } else {
            //TODO:存储离线消息
        }

    }
}
