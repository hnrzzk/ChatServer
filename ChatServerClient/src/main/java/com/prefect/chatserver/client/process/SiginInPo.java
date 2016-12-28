package com.prefect.chatserver.client.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.util.Interactive;
import com.prefect.chatserver.commoms.util.ChatMessage;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.UserInfo;
import org.apache.mina.core.session.IoSession;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by zhangkai on 2016/12/28.
 */

class SiginInPo implements MessagePo {
    Interactive interactive = Interactive.getInstance();

    public void process(IoSession ioSession, ChatMessage chatMessage) {
        ChatMessage message = new ChatMessage();

        message.setMessageType(MessageType.USER_MANAGE);
        message.setCommand(CommandType.USER_SIGN_IN);

        String jsonObject = JSON.toJSONString(getUserInfo());
        message.setMessage(jsonObject);
        message.setMessageLength(jsonObject.getBytes().length);

        ioSession.write(message);
    }

    private UserInfo getUserInfo() {
        Scanner scanner = new Scanner(System.in);
        UserInfo userInfo = new UserInfo();

        Console console = System.console();

        interactive.printlnToConsole("请输入用户信息～～");

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
