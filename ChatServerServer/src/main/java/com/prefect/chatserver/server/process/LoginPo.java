package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.server.util.db.DBUtil;
import com.prefect.chatserver.commoms.util.moudel.UserInfo;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import org.apache.mina.core.session.IoSession;

/**
 * 用户登录逻辑处理
 * Created by zhangkai on 2016/12/28.
 */
public class LoginPo extends ActionPo {
    public void process(IoSession ioSession, MessagePacket message) throws Exception {
        //将消息内容从json转换成object
        UserInfo userInfo = JSON.parseObject(message.getMessage(), UserInfo.class);

        String account = userInfo.getAccount();
        String password = userInfo.getPassword();

        if (!DBUtil.getInstance().isExit(
                "user",
                new String[]{"account", "password"},
                new Object[]{account, password})) { //用户不存在
            super.response(ioSession, CommandType.USER_LOGIN_ACK, false, "ERROR LOGIN: Account does not exist or Incorrect password.");
            return;
        } else if (DBUtil.getInstance().isExit(
                "user",
                new String[]{"account", "is_online"},
                new Object[]{account, 1})) { //用户已登录
            super.response(ioSession, CommandType.USER_LOGIN_ACK, false, "ERROR LOGIN: Account is logged in.");
            return;
        }

        //拼凑sql语句
        String updateOnlineStateSql = "update user set is_online=1 where account=?";

        if (DBUtil.getInstance().executeUpdate(updateOnlineStateSql, new Object[]{account}) != null) { //更新在线状态 成功
            //将已建立的连接保存在内存中
            ChatServerHandler.sessionMap.put(account, ioSession);
            //在session中记录account名称
            ioSession.setAttribute("account", account);

            super.response(ioSession, CommandType.USER_LOGIN_ACK, true, "SUCCESS LOGIN: Welcome!");
        } else {
            //更新在线状态失败
            super.response(ioSession, CommandType.USER_LOGIN_ACK, false, "ERROR LOGIN: Account does not exist or Incorrect password.");
            return;
        }

    }
}
