package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.moudel.RelationShipMessage;
import com.prefect.chatserver.server.db.DBUtil;
import com.prefect.chatserver.server.db.TableInfo.BlackListTable;
import org.apache.mina.core.session.IoSession;

import java.util.HashMap;
import java.util.Map;

/**
 * 移除黑名单类
 * Created by zhangkai on 2017/1/3.
 */
public class BlackListRemovePo extends ActionPo{
    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) {
        //类型转换 从json到object
        RelationShipMessage relationShipMessage = JSON.parseObject(messageObj.getMessage(), RelationShipMessage.class);

        String userAccount = relationShipMessage.getUserAccount();
        String friendAccount = relationShipMessage.getFriendAccount();

        removeBlackListRelationShip(userAccount,friendAccount);

        response(ioSession, CommandType.BLACK_LIST_REMOVE_ACK,true, "已成功将用户移出黑名单 account："+friendAccount);
    }

    private void removeBlackListRelationShip(String userAccount, String friendAccount) {
        Map<String, Object> map = new HashMap<>();
        map.put(BlackListTable.Field.userAccount, userAccount);
        map.put(BlackListTable.Field.blackAccount, friendAccount);
        DBUtil.getInstance().deleteRow(BlackListTable.name, map);
    }
}
