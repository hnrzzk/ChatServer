package com.prefect.chatserver.client.process.request.account;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.client.utils.Interactive;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.MessageType;
import com.prefect.chatserver.commoms.utils.moudel.UserInfo;
import org.apache.mina.core.session.IoSession;

import java.util.Scanner;

/**
 * 登录
 * Created by zhangkai on 2017/1/3.
 */
class LoginPo {
    public void requestLoginIn(IoSession ioSession) {
        MessagePacket message = new MessagePacket();

        message.setMessageType(MessageType.USER_INFO);
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
