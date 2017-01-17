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
 * 黑名单增加类
 * Created by zhangkai on 2017/1/3.
 */
public class BlackListAddPo extends ActionPo {
    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) {
        RelationShipMessage relationShipMessage = JSON.parseObject(messageObj.getMessage(), RelationShipMessage.class);

        String account = relationShipMessage.getUserAccount();
        String blackAccount = relationShipMessage.getFriendAccount();

        //给被请求者添加黑名单
        long blackListAdd = DBDao.getInstance().addBlackListInfo(
                account,
                blackAccount);

        //删除对应好友关系
        DBDao.getInstance().removeFriendRelationShip(account, blackAccount);
        DBDao.getInstance().removeFriendRelationShip(blackAccount, account);

        if (blackListAdd > 0) { //添加成功

            String key = account + "_" + blackAccount;
            ChatServer.blackListCache.put(key, true);

            response(ioSession, CommandType.BLACK_LIST_ADD_ACK, true,
                    String.format("Black list add request is accepted. accountInfo:[%s]", blackAccount));
        } else { //添加失败
            response(ioSession, CommandType.BLACK_LIST_ADD_ACK, false,
                    String.format("Black list add error. accountInfo:[%s]", blackAccount));
        }
    }
}
