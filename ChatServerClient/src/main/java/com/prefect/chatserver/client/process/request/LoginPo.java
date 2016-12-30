package com.prefect.chatserver.client.process.request;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.client.util.Interactive;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.UserInfo;
import org.apache.mina.core.session.IoSession;

import java.io.Console;
import java.util.Scanner;

/**
 * Created by zhangkai on 2016/12/28.
 */
public class LoginPo implements RequestPo {

    public void process(IoSession ioSession, MessagePacket messagePacket) {
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
