package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.utils.AttributeOperate;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.MessageType;
import com.prefect.chatserver.commoms.utils.moudel.ChatRoomMessage;
import com.prefect.chatserver.server.ChatServer;
import com.prefect.chatserver.server.db.hibernate.DBDao;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 用户登出处理逻辑
 * Created by zhangkai on 2016/12/30.
 */
public class LogOutPo extends ActionPo {
    private final static Logger logger = LoggerFactory.getLogger(LogOutPo.class);

    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) {

        //从session中得到账号
        String account = AttributeOperate.getInstance().getAccountOfAttribute(ioSession);
        String chatRoomName = AttributeOperate.getInstance().getChatRoomNameOfAttribute(ioSession);

        if (account != null && !account.isEmpty()) {
            //发送离线通知
            sendOfflineNotice(account);

            //修改用户离线状态
            if (!DBDao.getInstance().changeAccountOnlineStatus(account, 0)) {
                logger.error("修改用户状态操作失败：修改数据库状态失败");
            }

            //将离线用户的session从在线sessionMap中移除
            ChatServerHandler.sessionMap.remove(account);

            //修改对应聊天室消息
            quitChatRoom(account, chatRoomName, ioSession);
        }

    }

    /**
     * 发送用户下线通知
     *
     * @param
     */
    private void sendOfflineNotice(String account) {

        //根据得到该账号的在线好友列表
        List<String> accountList = DBDao.getInstance().getFriendListForOnlineStatus(account, 1);

        //生产消息字符串
        String message = new StringBuilder().append("您的好友").append(account).append("已下线").toString();

        //打包
        MessagePacket messagePacket = null;
        try {
            messagePacket = new MessagePacket(CommandType.USER_OFF_LINE_NOTICE, MessageType.STRING, message.getBytes("utf-8").length, message);
            //发送通知
            sendNotice(accountList, messagePacket);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * 将该session从对应的聊天室中清除
     *
     * @param chatRoomName
     * @param session
     */
    private void quitChatRoom(String account, String chatRoomName, IoSession session) {
        if (chatRoomName.isEmpty()) { //如果聊天室名字是null
            return;
        }

        CopyOnWriteArraySet<IoSession> sessionSet = ChatServer.chatRoomInfo.get(chatRoomName);
        if (null == sessionSet) {   //如果用户列表为null
            return;
        }

        sessionSet.remove(session);

        String message = new StringBuilder()
                .append("用户[").append(account).append("]")
                .append("离开聊天室")
                .toString();

        ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
        chatRoomMessage.setAccount(account);
        chatRoomMessage.setChatRoomName(chatRoomName);
        chatRoomMessage.setMessage(message);

        String json = JSON.toJSONString(chatRoomMessage);

        MessagePacket messagePacket = null;
        try {
            messagePacket = new MessagePacket(
                    CommandType.CHAT_ROOM_QUIT_ACK,
                    MessageType.CHATROOM_MANAGE,
                    json.getBytes("utf-8").length,
                    json);

            for (IoSession item : sessionSet) {
                if (item.isConnected()) {
                    item.write(messagePacket);
                }
            }

        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }


    }
}
