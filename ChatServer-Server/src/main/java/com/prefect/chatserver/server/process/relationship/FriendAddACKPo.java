package com.prefect.chatserver.server.process.relationship;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.moudel.RelationShipMessage;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import com.prefect.chatserver.server.db.hibernate.DBDao;
import com.prefect.chatserver.server.process.ActionPo;
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
    public void process(IoSession ioSession, MessagePacket messageObj) {
        RelationShipMessage relationShipMessage = JSON.parseObject(messageObj.getMessage(), RelationShipMessage.class);

        IoSession session = ChatServerHandler.sessionMap.get(relationShipMessage.getUserAccount());

        //对方接受好友请求
        if (relationShipMessage.isAccept()) {

            //给请求者添加好友
            long friendId = DBDao.getInstance().addFriendInfo(
                    relationShipMessage.getUserAccount(),
                    relationShipMessage.getFriendAccount(),
                    relationShipMessage.getCategoryName() );

            if (friendId > 0) { //好友添加成功
                response(session, CommandType.FRIEND_LIST_ADD_ACK, true,
                        String.format("Friend request is accepted. accountInfo:[%s]", relationShipMessage.getFriendAccount()));
            } else if (0==friendId){ //好友添加失败
                response(session, CommandType.FRIEND_LIST_ADD_ACK, false,
                        String.format("Friend is exist. accountInfo:[%s]", relationShipMessage.getFriendAccount()));
            }else {
                response(session, CommandType.FRIEND_LIST_ADD_ACK, false,
                        String.format("Friend add error. accountInfo:[%s]", relationShipMessage.getFriendAccount()));
            }

            //给被请求者添加好友
            friendId = DBDao.getInstance().addFriendInfo(
                    relationShipMessage.getFriendAccount(),
                    relationShipMessage.getUserAccount(),
                    defaultCategory);

            if (friendId > 0) { //好友添加成功
                response(ioSession, CommandType.FRIEND_LIST_ADD_ACK, true,
                        String.format("Friend request is accepted. accountInfo:[%s]", relationShipMessage.getUserAccount()));
            } else { //好友添加失败
                response(ioSession, CommandType.FRIEND_LIST_ADD_ACK, false,
                        String.format("Friend add error. accountInfo:[%s]", relationShipMessage.getFriendAccount()));
            }

        } else {
            response(session, CommandType.FRIEND_LIST_ADD_ACK, false,
                    String.format("Friend request is rejected. accountInfo:[%s]", relationShipMessage.getFriendAccount()));
        }
    }

}
