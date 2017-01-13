package com.prefect.chatserver.client.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.client.utils.Interactive;
import com.prefect.chatserver.client.utils.Util;
import com.prefect.chatserver.commoms.utils.*;
import com.prefect.chatserver.commoms.utils.moudel.ACKMessage;
import com.prefect.chatserver.commoms.utils.moudel.ChatMessage;
import com.prefect.chatserver.commoms.utils.moudel.UserInfo;
import com.prefect.chatserver.commoms.utils.moudel.UserLogin;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.Base64;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangkai on 2017/1/5.
 */
public class RobotResponsePo implements Runnable {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(RobotResponsePo.class);

    static int loginNum = 0;

    MessagePacket messagePacket;
    IoSession session;

    public RobotResponsePo(IoSession session, MessagePacket messagePacket) {
        this.messagePacket = messagePacket;
        this.session = session;
    }

    @Override
    public void run() {
        int command = messagePacket.getCommand();

        switch (command) {
            case CommandType.USER_LOGIN_REQUEST_ACK:
                verify();
                break;
            case CommandType.USER_LOGIN_VERIFY_ACK:
                processLogin();
                break;
            case CommandType.MESSAGE:
            case CommandType.SEND_BROADCAST_ACK:
                processMessage();
                break;
        }
    }

    private void verify() {
        ACKMessage ackMessage = JSON.parseObject(messagePacket.getMessage(), ACKMessage.class);
        String ackStr = ackMessage.getMessage();

        if (!ackMessage.getActionResult()) {
            Interactive.getInstance().printlnToConsole(ackStr);
            return;
        }

        String privateKey = AttributeOperate.getInstance().getPrivKey(session);

        try {
            byte[] bytes = Base64.getDecoder().decode(ackStr);

            String randomStr = new String(RSA.decryptByPrivateKey(bytes, privateKey));

            UserLogin userLogin = getUserInfo(randomStr);

            String json = JSON.toJSONString(userLogin);

            session.write(new MessagePacket(
                    CommandType.USER_LOGIN_VERIFY,
                    MessageType.USER_LOGIN,
                    json.getBytes("utf-8").length,
                    json));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private UserLogin getUserInfo(String randomStr) {

        UserLogin userInfo = new UserLogin();

        String account = "";
        while (account.equals("")) {
            account = AttributeOperate.getInstance().getAccountOfAttribute(session);
        }

        String password = account;

        userInfo.setAccount(account);
        System.out.println("account:" + account);

        String verify = MathUtil.getInstance().getMD5(password + randomStr);

        userInfo.setVerifyStr(verify);

        ChatClient.account = account;

        return userInfo;
    }

    /**
     * 处理服务器的登录反馈
     */
    private void processLogin() {
        ACKMessage ackMessage = JSON.parseObject(messagePacket.getMessage(), ACKMessage.class);
        //如果登录成功
        if (ackMessage.getActionResult()) {
            synchronized (this) {
                loginNum++;
            }

            Interactive.getInstance().printlnToConsole("登录成功,登录成功数：" + loginNum);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        MessagePacket messagePacket = new MessagePacket();
                        messagePacket.setCommand(CommandType.MESSAGE);
                        messagePacket.setMessageType(MessageType.MESSAGE);

                        String account = Util.getInstance().getAccount(session);
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setSendAccount(account);
                        chatMessage.setReceiveAccount(account);

                        String message = new StringBuilder()
                                .append(session.toString())
                                .append(" send: ")
                                .append(new Date(System.currentTimeMillis())).toString();
                        chatMessage.setMessage(message);

                        String json = JSON.toJSONString(chatMessage);
                        messagePacket.setMessage(json);
                        try {
                            messagePacket.setMessageLength(json.getBytes("utf-8").length);
                            session.write(messagePacket);

                            Thread.sleep(1000);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();

//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    MessagePacket messagePacket = new MessagePacket();
//                    messagePacket.setCommand(CommandType.MESSAGE);
//                    messagePacket.setMessageType(MessageType.MESSAGE);
//
//                    String account = Util.getInstance().getAccount(session);
//                    ChatMessage chatMessage = new ChatMessage();
//                    chatMessage.setSendAccount(account);
//                    chatMessage.setReceiveAccount(account);
//
//                    String message = new StringBuilder()
//                            .append(session.toString())
//                            .append(" send: ")
//                            .append(new Date(System.currentTimeMillis())).toString();
//                    chatMessage.setMessage(message);
//
//                    String json = JSON.toJSONString(chatMessage);
//                    messagePacket.setMessage(json);
//                    try {
//                        messagePacket.setMessageLength(json.getBytes("utf-8").length);
//                        session.write(messagePacket);
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }, 500, 1000);
        } else {
            Interactive.getInstance().printlnToConsole(ackMessage.getMessage());
        }
    }

    private void processMessage() {
        ChatMessage chatMessage = JSON.parseObject(messagePacket.getMessage(), ChatMessage.class);
        String account = chatMessage.getReceiveAccount();
        String sendAccount = chatMessage.getSendAccount();
        logger.info(account + " received message from " + sendAccount + " : " + chatMessage.getMessage());

    }
}
