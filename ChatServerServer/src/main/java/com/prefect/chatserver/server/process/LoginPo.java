package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.db.DBUtil;
import com.prefect.chatserver.commoms.util.moudel.UserInfo;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import org.apache.mina.core.session.IoSession;

/**
 * Created by zhangkai on 2016/12/28.
 */
public class LoginPo extends UserManagePo {
    public void handle(IoSession ioSession, MessagePacket message) throws Exception {
        //将消息内容从json转换成object
        UserInfo userInfo = JSON.parseObject(message.getMessage(), UserInfo.class);

        String account = userInfo.getAccount();
        String password = userInfo.getPassword();
        String nickName = userInfo.getAccount();

        //用户不存在
        if (!DBUtil.getInstance().isExit("user", new String[]{"account", "password"}, new Object[]{account, password})) {
            super.response(ioSession, CommandType.USER_LOGIN_ACK, "ERROR LOGIN: Account does not exist or Incorrect password.");
            return;
        }else if(DBUtil.getInstance().isExit("user", new String[]{"account", "is_online"}, new Object[]{account, 1})){
            super.response(ioSession, CommandType.USER_LOGIN_ACK, "ERROR LOGIN: Account is logged in.");
            return;
        }

        String updateOnlineStateSql = String.format("update user set %s='%s' where %s='%s'",
                "is_online", 1, "account", account);

        if (DBUtil.getInstance().executeUpdate(updateOnlineStateSql) > 0) { //跟新在线状态成功
            ChatServerHandler.sessionMap.put(account, ioSession);
            super.response(ioSession, CommandType.USER_LOGIN_ACK, "SUCCESS LOGIN: Welcome!");
        } else {
            //跟新在线状态失败
            super.response(ioSession, CommandType.USER_LOGIN_ACK, "ERROR LOGIN: Account does not exist or Incorrect password.");
            return;
        }

    }
}
