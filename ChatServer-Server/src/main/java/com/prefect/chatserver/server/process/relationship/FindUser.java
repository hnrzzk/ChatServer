package com.prefect.chatserver.server.process.relationship;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.MessageType;
import com.prefect.chatserver.commoms.utils.moudel.UserListInfo;
import com.prefect.chatserver.commoms.utils.moudel.UserInfo;
import com.prefect.chatserver.server.db.hibernate.DBDao;
import com.prefect.chatserver.server.process.ActionPo;
import org.apache.mina.core.session.IoSession;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 查找用户
 * Created by zhangkai on 2017/1/6.
 */
public class FindUser extends ActionPo {
    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) {
        UserInfo userInfo = JSON.parseObject(messageObj.getMessage(), UserInfo.class);
        //获取昵称
        String nickName = userInfo.getNickname();
        //获取账户
        String account = userInfo.getAccount();

        if (account != null) {
            findUserForAccount(ioSession, account);
        } else if (nickName != null) {
            findUserForNickName(ioSession, nickName);
        } else {
            response(ioSession, CommandType.USER_FIND_ACK, false, "用户信息输入不正确！");
        }
    }

    /**
     * 根据昵称查找用户
     *
     * @param ioSession
     * @param nickName
     */
    public void findUserForNickName(IoSession ioSession, String nickName) {

        List<UserInfo> userInfoList = DBDao.getInstance().findUserForNickName(nickName);
        if (userInfoList != null) {
            UserListInfo userListInfo = new UserListInfo();
            userListInfo.setFriendList(userInfoList);

            String json = JSON.toJSONString(userListInfo);
            MessagePacket messagePacket = null;
            try {
                messagePacket = new MessagePacket(CommandType.USER_FIND_ACK, MessageType.USER_LIST, json.getBytes("utf-8").length, json);
                ioSession.write(messagePacket);
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }


        } else {
            response(ioSession, CommandType.USER_FIND_ACK, false, "find friend failed!");
        }
    }

    /**
     * 根据账户名查找用户
     *
     * @param ioSession
     * @param account
     */
    public void findUserForAccount(IoSession ioSession, String account) {
        List<UserInfo> userInfoList = DBDao.getInstance().findUserForAccount(account);
        if (userInfoList != null) {
            UserListInfo userListInfo = new UserListInfo();
            userListInfo.setFriendList(userInfoList);

            String json = JSON.toJSONString(userListInfo);
            MessagePacket messagePacket = null;
            try {
                messagePacket = new MessagePacket(CommandType.USER_FIND_ACK, MessageType.USER_LIST, json.getBytes("utf-8").length, json);
                ioSession.write(messagePacket);
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }

        } else {
            response(ioSession, CommandType.USER_FIND_ACK, false, "find friend failed!");
        }
    }
}
