package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.db.DBUtil;
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
        String updateOnlineStateSql = String.format("update user set %s='%s' where %s='%s'",
                "is_online", 1, "account", account);

        if (DBUtil.getInstance().executeUpdate(updateOnlineStateSql) != null) { //更新在线状态 成功
            ChatServerHandler.sessionMap.put(account, ioSession);
            super.response(ioSession, CommandType.USER_LOGIN_ACK, true, "SUCCESS LOGIN: Welcome!");
        } else {
            //更新在线状态失败
            super.response(ioSession, CommandType.USER_LOGIN_ACK, false, "ERROR LOGIN: Account does not exist or Incorrect password.");
            return;
        }

    }
}
