package com.prefect.chatserver.server.process.relationship;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.moudel.RelationShipMessage;
import com.prefect.chatserver.server.db.hibernate.DBDao;
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
        //给被请求者添加黑名单
        long blackListAdd = DBDao.getInstance().addBlackListInfo(
                relationShipMessage.getUserAccount(),
                relationShipMessage.getFriendAccount());

        //删除对应好友关系
        DBDao.getInstance().removeFriendRelationShip(relationShipMessage.getUserAccount(), relationShipMessage.getFriendAccount());
        DBDao.getInstance().removeFriendRelationShip(relationShipMessage.getFriendAccount(), relationShipMessage.getUserAccount());

        if (blackListAdd > 0) { //添加成功
            response(ioSession, CommandType.BLACK_LIST_ADD_ACK, true,
                    String.format("Black list add request is accepted. accountInfo:[%s]", relationShipMessage.getFriendAccount()));
        } else { //添加失败
            response(ioSession, CommandType.BLACK_LIST_ADD_ACK, false,
                    String.format("Black list add error. accountInfo:[%s]", relationShipMessage.getFriendAccount()));
        }
    }
}
