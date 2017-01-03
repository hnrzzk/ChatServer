package com.prefect.chatserver.client.process.request.account;

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
                        new SignIn().requestSiginIn(session);
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

