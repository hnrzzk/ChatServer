package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.moudel.FriendManageMessage;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import com.prefect.chatserver.server.db.DBDao;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户a向用户b发送好友请求，用户b针对该请求的返回一个响应。
 * 该类处理服务器对用户b响应的处理逻辑
 * Created by zhangkai on 2016/12/29.
 */
public class FriendAddACKPo extends ActionPo {
    private final static Logger logger = LoggerFactory.getLogger(ChatServerHandler.class);

    public String defaultCategory = "好友";

    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) throws Exception {
        FriendManageMessage friendManageMessage = JSON.parseObject(messageObj.getMessage(), FriendManageMessage.class);

        IoSession session = ChatServerHandler.sessionMap.get(friendManageMessage.getUserAccount());
        if (session == null) {
        }

        //对方接受好友请求
        if (friendManageMessage.isAccept()) {

            //给请求者添加好友
            long friendId = DBDao.getInstance().addFriendInfo(
                    friendManageMessage.getUserAccount(), friendManageMessage.getFriendAccount(), friendManageMessage.getCategoryName());

            if (friendId > 0) { //好友添加成功
                response(session, CommandType.FRIEND_LIST_ADD_ACK, true,
                        String.format("Friend request is accepted. accountInfo:[%s]", friendManageMessage.getFriendAccount()));
            } else { //好友添加失败
                response(session, CommandType.FRIEND_LIST_ADD_ACK, false,
                        String.format("Friend add error. accountInfo:[%s]", friendManageMessage.getFriendAccount()));
            }

            //给被请求者添加好友
            friendId = DBDao.getInstance().addFriendInfo(
                    friendManageMessage.getFriendAccount(), friendManageMessage.getUserAccount(), defaultCategory);
            if (friendId > 0) { //好友添加成功
                response(ioSession, CommandType.FRIEND_LIST_ADD_ACK, true,
                        String.format("Friend request is accepted. accountInfo:[%s]", friendManageMessage.getUserAccount()));
            } else { //好友添加失败
                response(ioSession, CommandType.FRIEND_LIST_ADD_ACK, false,
                        String.format("Friend add error. accountInfo:[%s]", friendManageMessage.getFriendAccount()));
            }

        } else {
            response(session, CommandType.FRIEND_LIST_ADD_ACK, false,
                    String.format("Friend request is rejected. accountInfo:[%s]", friendManageMessage.getFriendAccount()));
        }
    }

}
