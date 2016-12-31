package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.moudel.FriendManageMessage;
import com.prefect.chatserver.server.db.DBDao;
import com.prefect.chatserver.server.db.DBUtil;
import com.prefect.chatserver.server.db.TableInfo.FriendsTable;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import org.apache.mina.core.session.IoSession;
import org.omg.CORBA.FREE_MEM;

import java.util.HashMap;
import java.util.Map;

/**
 * 好友删除处理逻辑
 * Created by zhangkai on 2016/12/30.
 */
public class FriendRemovePo extends ActionPo {
    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) throws Exception {
        //类型转换 从json到object
        FriendManageMessage friendManageMessage = JSON.parseObject(messageObj.getMessage(), FriendManageMessage.class);

        String userAccount = friendManageMessage.getUserAccount();
        String friendAccount = friendManageMessage.getFriendAccount();

        removeFriendRelationShip(userAccount,friendAccount);

        removeFriendRelationShip(friendAccount,userAccount);

        response(ioSession, CommandType.FRIEND_LIST_REMOVE_ACK,true, "已成功删除好友 account："+friendAccount);
    }

    void removeFriendRelationShip(String userAccount, String friendAccount) {
        Map<String, Object> map = new HashMap<>();
        map.put(FriendsTable.Field.userAccount, userAccount);
        map.put(FriendsTable.Field.friendAccount, friendAccount);
        DBUtil.getInstance().deleteRow(FriendsTable.name, map);
    }
}
