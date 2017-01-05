package com.prefect.chatserver.client.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.Robot;
import com.prefect.chatserver.client.util.Interactive;
import com.prefect.chatserver.commoms.util.AttributeOperate;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.UserInfo;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangkai on 2017/1/5.
 */
public class RobotPo {
    private static Logger logger = LoggerFactory.getLogger(RobotPo.class);

    private RobotPo() {

    }

    private static class RobotPoHandle {
        private static RobotPo instance = new RobotPo();
    }

    public static RobotPo getInstance() {
        return RobotPoHandle.instance;
    }

    /**
     * 机器人注册逻辑
     *
     * @param session
     */
    public void signIn(IoSession session) {

        //判断session中是否有account属性，如果没有等待一下
        String account = getAccount(session);

        //编辑报文
        MessagePacket messagePacket = new MessagePacket();
        messagePacket.setCommand(CommandType.USER_SIGN_IN);
        messagePacket.setMessageType(MessageType.USER_MANAGE);

        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
        userInfo.setPassword(account);
        userInfo.setNickname(account);

        String json = JSON.toJSONString(userInfo);
        messagePacket.setMessageLength(json.getBytes().length);
        messagePacket.setMessage(json);

        //发送报文
        session.write(messagePacket);
    }

    /**
     * 机器人登录逻辑
     *
     * @param session
     */
    public void login(IoSession session) {
        //判断session中是否有account属性，如果没有等待一下
        String account = getAccount(session);

        MessagePacket messagePacket = new MessagePacket();
        messagePacket.setCommand(CommandType.USER_LOGIN);
        messagePacket.setMessageType(MessageType.USER_MANAGE);

        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
        userInfo.setPassword(account);

        String json = JSON.toJSONString(userInfo);

        messagePacket.setMessageLength(json.getBytes().length);
        messagePacket.setMessage(json);

        session.write(messagePacket);
    }

    /**
     * 从session中获取账户名
     * @param session
     * @return
     */
    private String getAccount(IoSession session) {
        //判断session中是否有account属性，如果没有等待一下
        String account = "";
        while (account.equals("")) {
            account = AttributeOperate.getInstance().getAccountOfAttribute(session);
        }
        Interactive.getInstance().printlnToConsole("get account: " + account);

        return account;
    }


}
