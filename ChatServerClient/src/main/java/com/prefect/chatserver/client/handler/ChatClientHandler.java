package com.prefect.chatserver.client.handler;

import com.prefect.chatserver.client.process.interactive.UserInteractive;
import com.prefect.chatserver.client.process.request.account.AccountManagePo;
import com.prefect.chatserver.client.process.response.ResponsePo;
import com.prefect.chatserver.client.process.response.ResponsePoFactory;
import com.prefect.chatserver.commoms.utils.MessagePacket;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangkai on 2016/12/27.
 */
public class ChatClientHandler implements IoHandler {

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

        if (message instanceof MessagePacket) {
            MessagePacket messagePacket = (MessagePacket) message;

            ResponsePo responsePo = ResponsePoFactory.getClass(messagePacket.getCommand());

            responsePo.process(messagePacket);
        }

    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
    }

    @Override
    public void inputClosed(IoSession ioSession) throws Exception {
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
        //每500ms检查一次session是否建立成功
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (session.isConnected()) {

                    //建立连接时做出注册或者登录操作
                    new AccountManagePo().manangeUser(session);

                    //新建一个线程接受用户输入
                    UserInteractive userInteractive = new UserInteractive();
                    Thread thread = new Thread(userInteractive);
                    thread.start();

                    this.cancel();
                }
            }
        }, 0, 500);

    }
}
