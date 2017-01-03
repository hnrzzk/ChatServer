package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.moudel.RelationShipMessage;
import com.prefect.chatserver.server.db.DBDao;

import org.apache.mina.core.session.IoSession;


/**
 * 好友删除处理逻辑
 * Created by zhangkai on 2016/12/30.
 */
public class FriendRemovePo extends ActionPo {
    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) {
        //类型转换 从json到object
        RelationShipMessage relationShipMessage = JSON.parseObject(messageObj.getMessage(), RelationShipMessage.class);

        String userAccount = relationShipMessage.getUserAccount();
        String friendAccount = relationShipMessage.getFriendAccount();

        DBDao.getInstance().removeFriendRelationShip(userAccount,friendAccount);

        DBDao.getInstance().removeFriendRelationShip(friendAccount,userAccount);

        response(ioSession, CommandType.FRIEND_LIST_REMOVE_ACK,true, "已成功删除好友 account："+friendAccount);
    }
}
