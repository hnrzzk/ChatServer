package com.prefect.chatserver.client.process.request.account;

import org.apache.mina.core.session.IoSession;

import java.util.Scanner;

/**
 * Created by zhangkai on 2016/12/31.
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
                continue;
            }
        }
    }
}

