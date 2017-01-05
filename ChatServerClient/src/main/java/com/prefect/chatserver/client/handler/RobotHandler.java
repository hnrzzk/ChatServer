package com.prefect.chatserver.client.handler;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.Robot;
import com.prefect.chatserver.client.process.interactive.UserInteractive;
import com.prefect.chatserver.client.process.request.account.AccountManagePo;
import com.prefect.chatserver.client.util.Interactive;
import com.prefect.chatserver.commoms.util.AttributeOperate;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.UserInfo;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangkai on 2017/1/4.
 */
public class RobotHandler implements IoHandler {
    private static Logger logger = LoggerFactory.getLogger(RobotHandler.class);

    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {

    }

    @Override
    public void sessionOpened(IoSession ioSession) throws Exception {

        //每500ms检查一次session是否建立成功
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (ioSession.isConnected()) {

                    logger.info(Thread.currentThread().getName() + " 已建立连接，当前连接数:" + Robot.sessionConcurrentHashMap.size());

                    String account = "";
                    while (account.equals("")) {
                        account = AttributeOperate.getInstance().getAccountOfAttribute(ioSession);
                    }

                    Interactive.getInstance().printlnToConsole("get account: " + account);

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

                    ioSession.write(messagePacket);

                    this.cancel();
                }
            }
        }, 0, 500);
    }

    @Override
    public void sessionClosed(IoSession ioSession) throws Exception {
        Robot.sessionConcurrentHashMap.remove(ioSession.getAttribute("account"));
        logger.info(Thread.currentThread().getName() + " 连接已关闭，当前连接数:" + Robot.sessionConcurrentHashMap.size());

    }

    @Override
    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {

    }

    @Override
    public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {
        logger.info(Thread.currentThread().getName() + " session出现异常，当前连接数:" + Robot.sessionConcurrentHashMap.size());
    }

    @Override
    public void messageReceived(IoSession ioSession, Object o) throws Exception {

    }

    @Override
    public void messageSent(IoSession ioSession, Object o) throws Exception {

    }

    @Override
    public void inputClosed(IoSession ioSession) throws Exception {

    }
}
