package com.prefect.chatserver.client.process.request.account;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.client.utils.Interactive;
import com.prefect.chatserver.commoms.utils.AttributeOperate;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.MessageType;
import com.prefect.chatserver.commoms.utils.moudel.UserInfo;
import com.prefect.chatserver.commoms.utils.moudel.UserLogin;
import org.apache.mina.core.session.IoSession;

import java.util.Scanner;

/**
 * 登录
 * Created by zhangkai on 2017/1/3.
 */
class LoginPo {
    /**
     * 客户端请求登录，发送公钥
     * @param ioSession
     */
    public void requestLoginIn(IoSession ioSession) {
        MessagePacket message = new MessagePacket();

        message.setMessageType(MessageType.MESSAGE);
        message.setCommand(CommandType.USER_LOGIN_REQUEST);

        String pubKsy=AttributeOperate.getInstance().getPubKey(ioSession);
        while(null==pubKsy){
            pubKsy=AttributeOperate.getInstance().getPubKey(ioSession);
        }

        message.setMessage(pubKsy);
        message.setMessageLength(pubKsy.getBytes().length);

        ioSession.write(message);
    }


//    private UserLogin getUserInfo() {
//        Interactive interactive = Interactive.getInstance();
//
//        Scanner scanner = new Scanner(System.in);
//        UserLogin userInfo = new UserLogin();
//
//        interactive.printlnToConsole("～～欢迎登录～～");
//
//        interactive.printToConsole("请输入账户:");
//        String account = scanner.next();
//
//        interactive.printToConsole("请输入密码:");
//        String password = scanner.next();
//
//        userInfo.setAccount(account);
//        userInfo.setPassword(password);
//
//        ChatClient.account=account;
//
//        return userInfo;
//    }
}
