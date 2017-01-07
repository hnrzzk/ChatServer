package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.MessageType;
import com.prefect.chatserver.commoms.utils.moudel.ChatMessage;
import com.prefect.chatserver.commoms.utils.moudel.RelationShipMessage;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import com.prefect.chatserver.server.db.DBDao;
import org.apache.mina.core.session.IoSession;

/**
 * Created by zhangkai on 2016/12/29.
 */
public class MessagePo extends ActionPo {
    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) {
        switch (messageObj.getMessageType()) {
            case MessageType.MESSAGE:
                sendChatMessage(ioSession, messageObj);
                break;
            case MessageType.RELATIONSHIP_MANAGE:   //转发好友请求
                sendFriendAddRequest(ioSession, messageObj);
                break;
        }
    }

    /**
     * 发送聊天数据
     */
    private void sendChatMessage(IoSession ioSession, MessagePacket messageObj) {
        ChatMessage chatMessage = JSON.parseObject(messageObj.getMessage(), ChatMessage.class);

        String sendAccount = chatMessage.getSendAccount();
        String receiveAccount = chatMessage.getReceiveAccount();

        //判读是否被禁言
        if (DBDao.getInstance().isGag(sendAccount)) {
            response(ioSession, CommandType.MESSAGE_ACK, false, "Sorry, you have been gagged.");
            return;
        }

//        //判断是否在黑名单中
//        if (!DBDao.getInstance().isInBlackList(receiveAccount, sendAccount)) {
            IoSession receiveSecession = ChatServerHandler.sessionMap.get(chatMessage.getReceiveAccount());
            if (receiveSecession != null) { //如果好友在线则发送消息
                receiveSecession.write(messageObj);
            } else {
                //存储离线消息
                DBDao.getInstance().saveOfflineMessage(receiveAccount,messageObj);
            }
//        }
    }

    /**
     * 发送好友请求数据
     */
    private void sendFriendAddRequest(IoSession ioSession, MessagePacket messageObj) {
        RelationShipMessage relationShipMessage = JSON.parseObject(messageObj.getMessage(), RelationShipMessage.class);

        //判断是否在黑名单中
//        if (!DBDao.getInstance().isInBlackList(relationShipMessage.getFriendAccount(), relationShipMessage.getUserAccount())) {
            String friendAccount = relationShipMessage.getFriendAccount();

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
                //存储离线消息
                DBDao.getInstance().saveOfflineMessage(friendAccount,messageObj);
            }
//        }
    }
}
