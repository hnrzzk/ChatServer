package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.moudel.FriendInfo;
import com.prefect.chatserver.server.util.db.DBDao;
import org.apache.mina.core.session.IoSession;

/**
 * 增加好友逻辑类
 * Created by zhangkai on 2016/12/29.
 */
public class FriendAddPo extends ActionPo {
    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) throws Exception {
        FriendInfo friendInfo = JSON.parseObject(messageObj.getMessage(), FriendInfo.class);
        String categoryName = friendInfo.getCategoryName();
        String friendAccount = friendInfo.getFriendAccount();
        String userAccount = friendInfo.getUserAccount();

        if (!DBDao.getInstance().accountIsExist(friendAccount)) {
            response(ioSession, CommandType.FRIEND_LIST_ADD_ACK, false, "friendAccount does not exist!");
        }

        DBDao.getInstance().creatCategory(userAccount,categoryName);

    }
}
