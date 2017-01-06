package com.prefect.chatserver.client.process.request.account;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.utils.Interactive;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.MessageType;
import com.prefect.chatserver.commoms.utils.moudel.UserInfo;
import org.apache.mina.core.session.IoSession;

import java.util.Scanner;

/**
 * 注册
 * Created by zhangkai on 2017/1/3.
 */
class SignIn {
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