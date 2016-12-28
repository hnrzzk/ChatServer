package com.prefect.chatserver.server;

import org.apache.mina.core.session.IoSession;

import java.util.Date;

/**
 * Created by zhangkai on 2016/12/27.
 */
public class HandleThread implements Runnable{
    IoSession ioSession;
    Object message;

    public HandleThread(IoSession session, Object message){
        this.ioSession=session;
        this.message=message;
    }

    public void run() {
        String str = message.toString();
        if (str.trim().equalsIgnoreCase("quit")) {
            ioSession.closeNow();
            return;
        }
        Date date = new Date();
        ioSession.write(date.toString());
        System.out.println("Message written...");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
