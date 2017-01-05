package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.AttributeOperate;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.server.db.DBDao;
import com.prefect.chatserver.server.db.DBUtil;
import com.prefect.chatserver.commoms.util.moudel.UserInfo;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import com.prefect.chatserver.server.db.TableInfo.UserTable;
import org.apache.mina.core.session.IoSession;

import java.util.List;

/**
 * 用户登录逻辑处理
 * Created by zhangkai on 2016/12/28.
 */
public class LogInPo extends ActionPo {

    @Override
    public void process(IoSession ioSession, MessagePacket message) {
        //将消息内容从json转换成object
        UserInfo userInfo = JSON.parseObject(message.getMessage(), UserInfo.class);

        String account = userInfo.getAccount();
        String password = userInfo.getPassword();

        if (!DBUtil.getInstance().isExit(
                UserTable.name,
                new String[]{UserTable.Field.account, UserTable.Field.password},
                new Object[]{account, password})) { //用户不存在
            super.response(ioSession, CommandType.USER_LOGIN_ACK, false, "登录失败: Account does not exist or Incorrect password.");
            return;
        } else if (DBUtil.getInstance().isExit(
                UserTable.name,
                new String[]{UserTable.Field.account, UserTable.Field.isOnline},
                new Object[]{account, 1})) { //用户已登录
            super.response(ioSession, CommandType.USER_LOGIN_ACK, false, "登录失败: Account is logged in.");
            return;
        }

        if (DBDao.getInstance().changeAccountOnlineStatus(account, 1)) { //更新在线状态 成功
            //将已建立的连接保存在内存中
            ChatServerHandler.sessionMap.put(account, ioSession);
            //在session中记录account名称
            AttributeOperate.getInstance().setAccountOfAttribute(ioSession, account);

            super.response(ioSession, CommandType.USER_LOGIN_ACK, true, "登录成功: Welcome!");
        } else {
            //更新在线状态失败
            super.response(ioSession, CommandType.USER_LOGIN_ACK, false, "登录失败: Account does not exist or Incorrect password.");
            return;
        }

        //发送上线通知
        sendOnlineNotice(account);

    }

    void sendOnlineNotice(String account) {
        //根据得到该账号的在线好友列表
        List<String> accountList = DBDao.getInstance().getFriendLIst(account, 1);

        //生产消息字符串
        String message = new StringBuilder().append("您的好友").append(account).append("已上线").toString();

        //打包
        MessagePacket messagePacket = new MessagePacket(CommandType.USER_ON_LINE_NOTICE, MessageType.STRING, message.getBytes().length, message);

        //发送通知
        sendNotice(accountList, messagePacket);
    }
}
