package com.prefect.chatserver.server.process.relationship;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.moudel.RelationShipMessage;
import com.prefect.chatserver.server.ChatServer;
import com.prefect.chatserver.server.db.DBDao;
import com.prefect.chatserver.server.process.ActionPo;
import org.apache.mina.core.session.IoSession;

/**
 * 移除黑名单类
 * Created by zhangkai on 2017/1/3.
 */
public class BlackListRemovePo extends ActionPo {
    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) {
        //类型转换 从json到object
        RelationShipMessage relationShipMessage = JSON.parseObject(messageObj.getMessage(), RelationShipMessage.class);

        String userAccount = relationShipMessage.getUserAccount();
        String blackAccount = relationShipMessage.getFriendAccount();

        if (removeBlackListRelationShip(userAccount, blackAccount)) {
            String key = userAccount + "_" + blackAccount;
            ChatServer.blackListCache.put(key, false);

            response(ioSession, CommandType.BLACK_LIST_REMOVE_ACK, true, "已成功将用户移出黑名单 account：" + blackAccount);
        } else {
            response(ioSession, CommandType.BLACK_LIST_REMOVE_ACK, true, "系统错误，移除黑名单失败 account：" + blackAccount);
        }


    }

    private boolean removeBlackListRelationShip(String userAccount, String friendAccount) {
        return DBDao.getInstance().removeBlackRelationShip(userAccount, friendAccount);
    }
}
