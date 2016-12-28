package com.prefect.chatserver.client.handler;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.process.MessagePo;
import com.prefect.chatserver.client.process.PoFactory;
import com.prefect.chatserver.client.util.ClientCommandType;
import com.prefect.chatserver.commoms.util.ChatMessage;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.LoginMessage;

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

    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    public void messageReceived(IoSession session, Object message) throws Exception {
        long received = ((IoBuffer) message).getLong();
        if (received != this.counter.getCount()) {
            System.out.println("Error !");
            session.closeNow();
        } else if (this.counter.getCount() == 0L) {
            this.t1 = System.currentTimeMillis();
            System.out.println("------------->  end " + (this.t1 - this.t0));
            session.closeNow();
        } else {
            this.counter.countDown();
            this.buffer.flip();
            this.buffer.putLong(this.counter.getCount());
            this.buffer.flip();
            session.write(this.buffer);
        }

    }

    public void messageSent(IoSession session, Object message) throws Exception {
        System.out.println(message.toString());

    }

    public void sessionClosed(IoSession session) throws Exception {
    }

    public void sessionCreated(IoSession session) throws Exception {
    }

    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
    }

    public void sessionOpened(IoSession session) throws Exception {
        MessagePo po = PoFactory.getClass(init());
        if (po != null) {
            po.process(session, null);
        }
    }

    ClientCommandType init() {
        System.out.println("Welcome, please enty the number\nLoginin:1 Sigin:2");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String str = scanner.next();
                int command = Integer.parseInt(str);
                switch (command) {
                    case 1:
                        return ClientCommandType.Login;
                    case 2:
                        return ClientCommandType.Sigin;
                }
            } catch (NumberFormatException e) {
                continue;
            }
        }

    }
}
