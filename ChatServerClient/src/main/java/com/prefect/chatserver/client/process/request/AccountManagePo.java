package com.prefect.chatserver.client.process.request;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.client.util.Interactive;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.UserInfo;
import org.apache.mina.core.session.IoSession;

import java.util.Scanner;

/**
 * Created by liuxiaonan on 2016/12/31.
 */
public class AccountManagePo {
    public void manangeUser(IoSession session){
        System.out.println("Welcome, please enty the number\nLoginin:1 Sigin:2");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String str = scanner.next();
                int command = Integer.parseInt(str);
                switch (command) {
                    case 1:
                        new LoginPo().requestLoginIn(session);
                        return;
                    case 2:
                        new SiginInPo().requestSiginIn(session);
                        return;
                    default:
                        continue;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}

class LoginPo{

    public void requestLoginIn(IoSession ioSession) {
        MessagePacket message = new MessagePacket();

        message.setMessageType(MessageType.USER_MANAGE);
        message.setCommand(CommandType.USER_LOGIN);

        String jsonObject = JSON.toJSONString(getUserInfo());
        message.setMessage(jsonObject);
        message.setMessageLength(jsonObject.getBytes().length);

        ioSession.write(message);
    }

    private UserInfo getUserInfo() {
        Interactive interactive = Interactive.getInstance();

        Scanner scanner = new Scanner(System.in);
        UserInfo userInfo = new UserInfo();

        interactive.printlnToConsole("～～欢迎登录～～");

        interactive.printToConsole("请输入账户:");
        String account = scanner.next();

        interactive.printToConsole("请输入密码:");
        String password = scanner.next();

        userInfo.setAccount(account);
        userInfo.setPassword(password);

        ChatClient.account=account;

        return userInfo;
    }
}

class SiginInPo {

    public void requestSiginIn(IoSession ioSession) {
        MessagePacket message = new MessagePacket();

        message.setMessageType(MessageType.USER_MANAGE);
        message.setCommand(CommandType.USER_SIGN_IN);

        String jsonObject = JSON.toJSONString(getUserInfo());
        message.setMessage(jsonObject);
        message.setMessageLength(jsonObject.getBytes().length);

        ioSession.write(message);
    }

    private UserInfo getUserInfo() {
        Interactive interactive = Interactive.getInstance();

        Scanner scanner = new Scanner(System.in);
        UserInfo userInfo = new UserInfo();

        interactive.printlnToConsole("～～谢谢注册～～");

        interactive.printToConsole("请输入账户:");
        String account = scanner.next();

        interactive.printToConsole("请输入密码:");
        String password = scanner.next();

        interactive.printToConsole("请输入昵称:");
        String nickName = scanner.next();

        userInfo.setAccount(account);
        userInfo.setPassword(password);
        userInfo.setNickname(nickName);

        return userInfo;
    }
}

