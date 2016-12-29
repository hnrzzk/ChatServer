package com.prefect.chatserver.client.handler;

import com.prefect.chatserver.client.process.UserInputPo;
import com.prefect.chatserver.client.process.request.RequestPo;
import com.prefect.chatserver.client.process.request.RequestPoFactory;
import com.prefect.chatserver.client.process.response.ResponsePo;
import com.prefect.chatserver.client.process.response.ResponsePoFactory;
import com.prefect.chatserver.client.util.ClientRequestType;
import com.prefect.chatserver.commoms.util.MessagePacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;


import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zhangkai on 2016/12/27.
 */
public class ChatClientHandler extends IoHandlerAdapter {
    private IoConnector connector = new NioSocketConnector();
    private static IoSession session;
    private IoBuffer buffer = IoBuffer.allocate(1000);
    private long t0;
    private long t1;
    private CountDownLatch counter;

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
        RequestPo po = RequestPoFactory.getClass(init());
        if (po != null) {
            po.process(session, null);
        }

        UserInputPo userInputPo = new UserInputPo();
        Thread thread = new Thread(userInputPo);
        thread.start();
    }

    ClientRequestType init() {
        System.out.println("Welcome, please enty the number\nLoginin:1 Sigin:2");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String str = scanner.next();
                int command = Integer.parseInt(str);
                switch (command) {
                    case 1:
                        return ClientRequestType.Login;
                    case 2:
                        return ClientRequestType.Sigin;
                }
            } catch (NumberFormatException e) {
                continue;
            }
        }

    }
}
