package com.prefect.chatserver.client.handler;

import com.prefect.chatserver.client.process.interactive.UserInteractive;
import com.prefect.chatserver.client.process.request.LoginPo;
import com.prefect.chatserver.client.process.request.RequestPo;
import com.prefect.chatserver.client.process.request.SiginInPo;
import com.prefect.chatserver.client.process.response.ResponsePo;
import com.prefect.chatserver.client.process.response.ResponsePoFactory;
import com.prefect.chatserver.commoms.util.MessagePacket;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.Scanner;

/**
 *
 * Created by zhangkai on 2016/12/27.
 */
public class ChatClientHandler extends IoHandlerAdapter {

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

        if (message instanceof MessagePacket) {
            MessagePacket messagePacket = (MessagePacket) message;
            messagePacket.getMessageType();

            ResponsePo requestPo = ResponsePoFactory.getClass(messagePacket.getCommand());

            requestPo.process(messagePacket);
        }

    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        System.out.println(message.toString());

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        RequestPo po = init();
        if (po != null) {
            po.process(session, null);
        }

        UserInteractive userInteractive = new UserInteractive();
        Thread thread = new Thread(userInteractive);
        thread.start();
    }

    RequestPo init() {
        System.out.println("Welcome, please enty the number\nLoginin:1 Sigin:2");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String str = scanner.next();
                int command = Integer.parseInt(str);
                switch (command) {
                    case 1:
                        return new LoginPo();
                    case 2:
                        return new SiginInPo();
                    default:
                        return null;
                }
            } catch (NumberFormatException e) {
                continue;
            }
        }

    }
}
